![Composter](https://i.imgur.com/uZATZq4.png)
# Composter
## A multithreaded cleanroom implementation of the Minecraft Beta 1.7.3 server.
![CI](https://github.com/nkomarn/composter/workflows/Java%20CI/badge.svg) [![CodeFactor](https://www.codefactor.io/repository/github/nkomarn/composter/badge)](https://www.codefactor.io/repository/github/nkomarn/composter) [![License](https://img.shields.io/badge/license-MIT-brightgreen.svg)](https://github.com/nkomarn/Harbor/blob/master/LICENSE)

Composter is still **extremely early in development.** While I will probably end up making a majority of this project, I definitely will need help along the way. This is an ambitious project, and I do realize that, but it is definitely an achievable one. I am starting this project for a few reasons:
1. I want to learn about creating server software from scratch
2. I seek to learn more about multithreaded software and parallelism
3. I like Minecraft Beta 1.7.3 and it's old enough to be a reasonable target for this project (*I think*)
4. If Notch could do it, I probably can (*actually probably not*)
5. I enjoy pain **(╯°□°）╯︵ ┻━┻**

### 📋 Bucket list
* ~~Use [Reactor Netty](https://github.com/reactor/reactor-netty) instead of just raw Netty. This will hopefully bring some performance improvements~~
* Close to the same generation that Beta has. I know this is a pretty unreasonable goal, but hey, why not.
* Use a different compression algorithm for chunks. I am looking at either [zstd](https://facebook.github.io/zstd/) or [lz4](https://github.com/lz4/lz4).
* Async whatever the hell I can async.
* Maybe write a cleaner world save system.
* Get someone else to write the physics stuff (lol)
* Other stuff, mainly consisting of finishing this project at some point with reasonable quality ✨

Have a look at the [project tracker](https://github.com/nkomarn/Composter/projects/1) for todos and ongoing projects.

### 📚 Resources 
* [wiki.vg](https://wiki.vg/index.php?title=Protocol&oldid=689) - Reverse engineered Beta protocol (mostly)
* [Protocol FAQ](https://wiki.vg/index.php?title=Protocol_FAQ&oldid=74) - Typical flow of the Beta protocol
* [NBT Format](https://web.archive.org/web/20110204151459/http://www.minecraft.net/docs/NBT.txt) - An old archived document outlining the NBT format
* ~~[Reactor Netty docs](https://projectreactor.io/docs/netty/release/reference/index.html#tcp-server) - Great documentation outlining a lot of the features of Reactor~~
* 🌎 *Lots and lots of Googling...*

### 📰 Licensing stuff
This project is licensed under the MIT license. I hope to not only create something functional out of this, but also allow others to learn from the code and improve their own knowledge! All of the code in this project will be completely original and thus you can copy and modify anything as you wish for your own purposes!

---

This project will not be possible without the support of many others. I appreciate any and all contributions made to this project. Together, we can create something great! :)
