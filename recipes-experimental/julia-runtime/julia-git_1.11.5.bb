SUMMARY = "Julia is a high-level, high-performance dynamic programming language for technical computing"
HOMEPAGE = "https://julialang.org/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=172fb32b02b335508e62c04972eaad3f"

PV = "1.11.5"

SRC_URI = " \
    https://github.com/JuliaLang/julia/releases/download/v${PV}/julia-${PV}-full.tar.gz \
    file://Make.user \
    "
SRC_URI[sha256sum] = "147626db6b6887bfb6612cb6cd2a48d689806649fae34dc749fddc8375c2c625"

FORTRAN = ",fortran"
FORTRAN:forcevariable = ",fortran"

EXTERNAL_LIBS = " \
    utf8proc \
    openblas \
    suitesparse \
    mpfr \
    gmp \
    libpcre \
    libpcre2 \
    libgit2 \
    curl \
    libssh2 \
    openssl \
    zlib \
    lapack \
    mbedtls \
    libunwind \
"

BUILD_TOOLS = " \
    libgfortran \
    gcc \
    cmake-native \
    python3-native \
    perl-native \
"

BBCLASSEXTEND = "native nativesdk"

inherit pkgconfig bin_package autotools-brokensep

# Required compilers and tools
DEPENDS += " ${BUILD_TOOLS} ${EXTERNAL_LIBS}"

# Runtime dependencies
RDEPENDS{PV} += "${EXTERNAL_LIBS}"

S = "${WORKDIR}/${BPN}-${PV}"
B = "${WORKDIR}/${BPN}-${PV}"

# Julia's build system has some special flags

TAR := "${STAGING_BINDIR_TARGET}/tar --no-same-owner"

do_install() {
    # setup logs
    echo "-- New Install Run --\n" > run_do_install.log
    echo "VERSION: 0.0.2" >> run_do_install.log
    echo "Current Directory: ${PWD}" >> run_do_install.log
    echo "\n=== === ===\n" >> run_do_install.log

    # add make user file
    cp ${WORKDIR}/Make.user Make.user
    # update Make.user with additional flags
    echo "prefix=${prefix}" >> Make.user
    echo "bindir=${bindir}" >> Make.user
    echo "ibdir=${libdir}" >> Make.user
    echo "includedir=${includedir}" >> Make.user
    echo "sysconfdir=${sysconfdir}" >> Make.user
    echo "datarootdir=${datadir}" >> Make.user
    echo "datadir=${datadir}/julia" >> Make.user
    echo "CFLAGS=$(echo ${TARGET_CFLAGS} | sed 's/-fcanon-prefix-map[[:space:]]*//g')" >> Make.user
    echo "CXXFLAGS=$(echo ${TARGET_CXXFLAGS} | sed 's/-fcanon-prefix-map[[:space:]]*//g')" >> Make.user
    echo "LDFLAGS=${TARGET_LDFLAGS}" >> Make.user
    echo "TAR=${TAR}" >> Make.user
    echo "USE_SYSTEM_BLAS=1" >> Make.user

    cat Make.user >> run_do_install.log
    echo "\n===\n" >> run_do_install.log
    ls >> run_do_install.log
    echo "\n===\n" >> run_do_install.log

    # compile julia runtime
    oe_runmake USE_SYSTEM_BLAS=1 release

    # install julia runtime
    install -d ${D}${prefix}/julia
    # cp -a ${B} ${D}${prefix}/julia/
}
