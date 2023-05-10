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
SRC_URI = "https://github.com/JuliaLang/julia/archive/refs/tags/v1.6.7.tar.gz"
SRC_URI[sha256sum] = "384d039d96b33f463c5551446a80ee8f56b23526d9870633576f8f63fe919001"

# The vendor will typically ship release builds without debug symbols.
# Avoid errors by preventing the packaging task from stripping out the symbols and adding them to a separate debug package.
# This is done by setting the 'INHIBIT' flags shown below.
#INHIBIT_PACKAGE_STRIP = "1"
#INHIBIT_SYSROOT_STRIP = "1"
#INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

# skip qa sections
# already-stripped - for libexec/7z
# ldflags - the checksum for this package is different from the julia lib checksum
# dev-so - allow symlink .so files to be added in a non-dev package
#INSANE_SKIP:${PN} += "already-stripped ldflags dev-so"
#INSANE_SKIP:${PN}-dev += "already-stripped ldflags"

# specify dependancies
# GNU make — building dependencies.
# gcc & g++ (>= 5.1) or Clang (>= 3.5, >= 6.0 for Apple Clang) — compiling and linking C, C++.
# libatomic — provided by gcc and needed to support atomic operations.
# python (>=2.7) — needed to build LLVM.
# gfortran — compiling and linking Fortran libraries.
# perl— preprocessing of header files of libraries.
# wget, curl, or fetch (FreeBSD) — to automatically download external libraries.
# m4 — needed to build GMP.
# awk — helper tool for Makefiles.
# patch — for modifying source code.
# cmake (>= 3.4.3) — needed to build libgit2.
# pkg-config — needed to build libgit2 correctly, especially for proxy support.
# powershell (>= 3.0) — necessary only on Windows.
# which — needed for checking build dependencies.

DEPENDS += "make libgcc gcc-runtime libatomic python gfortran perl wget awk m4 patch cmake pkg-config which"
RDEPENDS:${PN} += "libgcc libstdc++"

# specify the order for packages to be created in
PACKAGES = "${PN}-dbg ${PN} ${PN}-doc ${PN}-dev"

do_compile() {
   make -j 6 
}

do_install() {
    # TODO: copy files over

    # Install `/etc/profile.d` for julia system configuration
    install -d ${D}/etc/profile.d
    ## create profile file and set important variables
    echo "export JULIA_BINDIR='/usr/bin'" > ${WORKDIR}/julia.sh
    echo "export JULIA_DEPOT_PATH='/var/opt/julia'" >> ${WORKDIR}/julia.sh
    # install julia profile
    install -Dm 0755 ${WORKDIR}/julia.sh ${D}/etc/profile.d/
}

# specifiy the files for each package
## Debug Packages
FILES:${PN}-dbg += ""
## Base Packages
FILES:${PN} += "/etc/profile.d/julia.sh ${datadir}/appdata"
## Docs Packages
FILES:${PN}-doc += ""
## Development Packages
FILES:${PN}-dev += ""
