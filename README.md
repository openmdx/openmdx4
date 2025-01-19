# Welcome to openMDX 4

The purpose of this repository is to ensure the simplest possible migration from `openMDX for Jakarta EE 8` to `openMDX for Jakarta EE 10`.

This repository is a fork of the `openMDX 4` flavour (`-Pflavour=4`) of the multi-source edition of [openMDX](https://github.com/openmdx/openmdx). However, this repository does not use a [Java precompiler](https://github.com/manifold-systems/manifold/tree/master/manifold-deps-parent/manifold-preprocessor) and allowed all `#if #else #endif` directives to be removed. This greatly simplifies the build process and the support of various IDEs without specific plugins. The repositories `./openmdx` and `./openmdx4` will be merged in the future as soon as the single-source support is not rquired anymore.

Features will ported from [openMDX](https://github.com/openmdx/openmdx) on a regular basis.

For documentation see [here](https://github.com/openmdx/openmdx-documentation/blob/master/README.md)
