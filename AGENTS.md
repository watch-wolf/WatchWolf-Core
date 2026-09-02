# AGENTS.md — WatchWolf-Core

Shared library for the WatchWolf framework: the domain entities every module exchanges, the RPC
runtime that carries them over a socket, and the code generator that turns the
[API definitions](https://github.com/watch-wolf/WatchWolf/tree/main/API/definitions) into Java
stubs.

`dev.watchwolf:watchwolf-core` · **Java 8** · Maven · published to GitHub Packages
(`maven.pkg.github.com/miranda1000/watchwolf-core`).

It is consumed by WatchWolf-ServersManager (and eventually by the other modules — see
[Migration state](#migration-state)).

## Layout

```
src/main/java/dev/watchwolf/
├── core/entities/          domain model
│   ├── blocks/             Block, the property mixins (Ageable, Openable, …) and transformer/
│   ├── blocks/special/     562 GENERATED block classes
│   ├── entities/           Entity + one class per mob, and the EntityType enum (GENERATED)
│   ├── files/              ConfigFile, ZipFile, WorldFile, plugins/{Usual,Uploaded,File}Plugin
│   ├── items/              Item, ItemType
│   └── Position, ServerType, WorldType, Difficulty, Version, Container
├── core/rpc/
│   ├── channel/            MessageChannel abstraction + sockets/{client,server} implementations
│   ├── objects/            RPCObject type system and the RPCConverter marshalling tree
│   ├── stubs/<module>/     GENERATED local stubs + petition/event interfaces
│   └── RPC, RPCFactory, RPCImplementer
├── core/utils/             DockerUtilities (MC version -> JDK), Version
├── client|clientsmanager|server|serversmanager|tester/
│                           the per-module petition interfaces (the protocol surface)
└── src/scripts/java/dev/watchwolf/rpc/
                            RPCComponentsCreator — the stub generator (not part of the library)

src/test/java/              unit tests            (*Should.java)
src/integration-test/java/  integration tests     (IT*.java)
```

## Build, test, generate

Everything runs inside Docker so the host needs no JDK/Maven.

```bash
./ci/build.sh [--preclean]        # mvn compile assembly:single -> target/watchwolf-core-<v>.jar
./ci/tests.sh --unit              # surefire; reports in target/surefire-reports + target/site
./ci/tests.sh --integration       # failsafe;  reports in target/failsafe-reports + target/site
./ci/tests.sh --unit --tests 'VersionShould'   # filter
./ci/validator.sh                 # test-naming lint; run before opening a PR
./ci/rpc-gen.sh                   # regenerate the RPC stubs (see below)
```

The produced jar is a `jar-with-dependencies` with `appendAssemblyId=false`, so it is a drop-in
`lib/watchwolf-core-<version>.jar` for the downstream modules.

## Conventions

- **Test naming is enforced.** Unit tests are `*Should.java` under `src/test/java`; integration
  tests are `IT*.java` under `src/integration-test/java` **and must carry `@Timeout` on the line
  immediately before `public class`** (`ci/validator.sh` greps for exactly that). A misnamed file
  silently never runs.
- **Maven profiles.** `default` runs unit tests only; `-P integration-test` runs integration
  tests only. `src/integration-test/java` and `src/scripts/java` are attached as extra source
  roots by `build-helper-maven-plugin`.
- **Logging is log4j2**, with `logger.traceEntry(...)` / `logger.traceExit(...)` at method
  boundaries and `CloseableThreadContext` around long-lived loops. Match that when adding code to
  `core/rpc`.
- Bump `<version>` in `pom.xml` when the jar changes; downstream `pom.xml`s pin it exactly.

## Generated code — do not hand-edit

Two generators write into `src/main/java`:

| Path | Generator | Trigger |
| --- | --- | --- |
| `core/rpc/stubs/**` and `<module>/rpc/**` | `dev.watchwolf.rpc.RPCComponentsCreator` (this repo) | `./ci/rpc-gen.sh` |
| `core/entities/blocks/special/**`, `core/entities/entities/EntityType.java` | [WatchWolf-MaterialGetter](https://github.com/miranda1000/WatchWolf-MaterialGetter) | run that plugin on a Spigot server |

Generated files carry a `/!\ Class generated automatically; do not modify /!\` header.

`ci/rpc-gen.sh` deletes `ServersManagerLocalStub.java`, rebuilds, then runs the generator.
**The definitions it reads are pinned by URL**, including a commit SHA, in
`src/scripts/java/dev/watchwolf/rpc/DefinitionDataFactory.java#LATEST_DEFINITIONS_LIST`. Editing
the JSON in the WatchWolf repo does nothing here until that SHA is bumped. It also needs network
access at generation time.

## RPC in one paragraph

`RPCFactory.build(implementerFactory, channelFactory)` wires a generated `…LocalStub`
(`RPCImplementer`) to a `MessageChannel` and an `RPCConverter`. `RPC.createConnection()` blocks
until a peer connects; `RPC.run()` then loops forwarding inbound bytes to
`RPCImplementer.forwardCall(channel, converter)`, while `RPC.sendEvent(...)` pushes async events
back. The stub decodes the 16-bit header itself:

```java
short info = converter.unmarshall(channel, Short.class);
byte  origin    = (byte)(info & 0b111);      // DST
boolean isReturn = (info & 0b1_000) > 0;     // r
short operation  = (short)(info >> 4);
```

`RPCConverter` is a tree: a `MainSubconverter` delegates to per-type subconverters
(`RPCString`, `RPCArray`, `RPCEnum`, `RPCPosition`, `RPCPlugin`, …) and throws
`UnsupportedOperationException` when no converter claims a type. Adding a new argument type means
adding an `RPCObject` under `core/rpc/objects/types/` **and** registering its subconverter.

## Migration state

There are two generations of the shared library in the wild:

- **New:** this repo — `dev.watchwolf.core.*`, socket RPC, code-generated stubs. Used by
  WatchWolf-ServersManager.
- **Old:** `dev.watchwolf.entities.*` / `dev.watchwolf.tester.*`, which still lives inside
  [WatchWolf-Tester](https://github.com/miranda1000/WatchWolf-Tester) and is what
  WatchWolf-Server links against (`lib/watchwolf-tester-0.2.1.jar`).

The two class trees are near-duplicates (both carry the same 562 generated block classes). When
changing an entity, check whether the Tester copy needs the same change.
