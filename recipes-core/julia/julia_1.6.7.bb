SUMMARY = "julia - high-level, high-performance dynamic programming language for technical computing"
DESCRIPTION = "Julia is a high-level, high-performance dynamic programming language for technical computing, \
with syntax that is familiar to users of other technical computing environments. It provides a sophisticated compiler, distributed \
parallel execution, numerical accuracy, and an extensive mathematical function library. \
The library, largely written in Julia itself, also integrates mature, best-of-breed C and Fortran libraries for linear algebra, \
random number generation, signal processing, and string processing. \
In addition, the Julia developer community is contributing a number of external packages through Julia's built-in package manager at a rapid pace. Julia \
programs are organized around multiple dispatch; by defining functions and overloading them for different combinations of argument types, \
which can also be user-defined."

# Any non-closed lisesce requires checksum
# TODO: Lisense should be updated to use the Julia license included in the downloaded files
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

JULIA_VERSION="1.6.7"
JULIA_ARCH="aarch64"
JULIA_RELEASE="1.6" 

# Yocto will download and extract the uri. the sha256sum is required to validate.
SRC_URI = "https://julialang-s3.julialang.org/bin/linux/${JULIA_ARCH}/${JULIA_RELEASE}/julia-${JULIA_VERSION}-linux-${JULIA_ARCH}.tar.gz"
SRC_URI[sha256sum] = "8746d561cbe35e1b83739a84b2637a1d2348728b1d94d76629ad98ff76da6cea"
#case "$JULIA_ARCH" in
#  aarch64) _pkgarch="aarch64"
#    #SRC_URI = "https://julialang-s3.julialang.org/bin/linux/${JULIA_ARCH}/${JULIA_RELEASE}/julia-${JULIA_VERSION}-linux-${JULIA_ARCH}.tar.gz"
#    SRC_URI[sha256sum] = "8746d561cbe35e1b83739a84b2637a1d2348728b1d94d76629ad98ff76da6cea"
#    ;;
#  x86_64) _pkgarch="x64"
#    #SRC_URI = "https://julialang-s3.julialang.org/bin/linux/${JULIA_ARCH}/${JULIA_RELEASE}/julia-${JULIA_VERSION}-linux-${JULIA_ARCH}.tar.gz"
#    SRC_URI[sha256sum] = "6c4522d595e4cbcd00157ac458a72f8aec01757053d2073f99daa39e442b2a36"
#    ;;
#esac

# The vendor will typically ship release builds without debug symbols.
# Avoid errors by preventing the packaging task from stripping out the symbols and adding them to a separate debug package.
# This is done by setting the 'INHIBIT' flags shown below.
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

# skip qa sections
# already-stripped - for libexec/7z
# ldflags - the checksum for this package is different from the julia lib checksum
# dev-so - allow symlink .so files to be added in a non-dev package
INSANE_SKIP_${PN} += "already-stripped ldflags dev-so"
INSANE_SKIP_${PN}-dev += "already-stripped ldflags"

# specify dependancies
DEPENDS += "libgcc gcc-runtime"
RDEPENDS_${PN} += "libgcc libstdc++"

# specify the order for packages to be created in
PACKAGES = "${PN}-dbg ${PN} ${PN}-doc ${PN}-dev"

do_install() {
    ## install base directories
    install -d ${D}${bindir} # /opt/julia/bin
    install -d ${D}${sysconfdir} # /opt/julia/etc
    install -d ${D}${includedir} # /opt/jula/include
    install -d ${D}${libdir} # /opt/julia/lib
    install -d ${D}${libexecdir} # /opt/julia/libexec 
    install -d ${D}${datadir} # /usr/share
    install -d ${D}${datadir}/${PN} # /usr/share/julia
}

# specifiy the files for each package
## Debug Packages
FILES_${PN}-dbg += ""
## Base Packages
FILES_${PN} += ""
## Docs Packages
FILES_${PN}-doc += ""
## Development Packages
FILES_${PN}-dev += ""