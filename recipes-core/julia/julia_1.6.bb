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

# specifiy the files for each package
FILES_${PN} = "${base_prefix}/opt/${PN}/* ${base_prefix}/usr/share/licenses/${PN}/*"

do_install() {
    install -d ${D}{base_prefix}/opt/${PN}          
    install -d ${D}{base_prefix}/usr/share/licenses/${PN}
    cp -R --no-dereference --preserve=mode,links -v ${S}{bin,etc,include,lib,share,libexec} ${D}/opt/${PN}/
    install -Dm644 ${S}/LICENSE.md ${D}/usr/share/licenses/${PN}/LICENSE.md

    # add system env variables for julia
    install -d ${D}/etc/profile.d
    # create profile file
    echo "export JULIA_DEPOT_PATH='/opt/julia'" > ${WORKDIR}/set_julia_env_vars
    install -m 0755 ${WORKDIR}/set_julia_env_vars ${D}/etc/profile.d/set_julia_env_vars.sh
}