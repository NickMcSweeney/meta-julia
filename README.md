## Introduction

This OpenEmbedded layer provides the Julia runtime that installs from the prebuilt language package at the [Julia language download page](https://julialang.org/downloads/).

There is an additional set of (in-development) recipes for building the Julia runtime from source.

## Core
### Julia runtime

The julia runtime is installed as `julia` and the version can be set with `PREFERRED_VERSION_julia = "x.y"` in your layer.conf or distro files.

Julia is installed to `/opt/julia` with a simlink to `/usr/bin/julia` for the executable. This is done because it allows an easy installation of the prebuilt `.so` files in a way that is "sandboxed" from the rest of the system to not conflict with system versions of the libraries. This is important because Julia ships with a set of prebuilt libraries that are often patched/modified versions of the system libraries.

## Experimental
### Julia from source

The clear solution to the issues installing Julia libraries is to build Julia from source. Building Julia from source is complex due to the issues with Julia's cross-compilation support.

Source installation is done by setting julia's makefile variables to use the system version of the libraries with `USE_SYSTEM_<LIBNAME> = 1` and providing a recipe for each library that uses the same source as Julia.

To experiment with building Julia from source, use the `julia-git` recipe.

## Reference Notes
### Variable refrence
```
prefix	        /usr                    /usr
base_prefix	(empty)	                (empty)
exec_prefix	${base_prefix}	        (empty)
base_bindir	${base_prefix}/bin	/bin
base_sbindir	${base_prefix}/sbin	/sbin
base_libdir	${base_prefix}/lib	/lib
datadir        ${prefix}/share	        /usr/share
sysconfdir	/etc	                /etc
localstatedir	/var	                /var
infodir        ${datadir}/info	        /usr/share/info
mandir         ${datadir}/man	        /usr/share/man
docdir         ${datadir}/doc	        /usr/share/doc
servicedir	/srv	                /srv
bindir         ${exec_prefix}/bin	/usr/bin
sbindir        ${exec_prefix}/sbin	/usr/sbin
libexecdir	${exec_prefix}/libexec	/usr/libexec
libdir         ${exec_prefix}/lib	/usr/lib
includedir	${exec_prefix}/include	/usr/include
palmtopdir	${libdir}/opie	        /usr/lib/opie
palmqtdir	${palmtopdir}	        /usr/lib/opie
```
