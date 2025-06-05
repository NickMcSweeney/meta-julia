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
    gcc-runtime \
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

S = "${WORKDIR}/julia-${PV}"

EXTRA_OEMAKE = "\
    PREFIX=${prefix} \
    CC='${CC}' \
    CFLAGS='${TARGET_CFLAGS}' \
    CXXFLAGS='${TARGET_CXXFLAGS}' \
    LDFLAGS=${TARGET_LDFLAGS} \
    LD_LIBRARY_PATH=${RECIPE_SYSROOT}${libdir}:${RECIPE_SYSROOT}${includedir}:${LD_LIBRARY_PATH} \
    DESTDIR=${D} \
    LIBDIR=${libdir} \
    INCLUDEDIR=${includedir} \
    BUILDROOT=${S} \
    BUILD_STATIC=no \
    ARCH=aarch64 \
    MARCH=aarch64 \
    MCPU=aarch64 \
    JULIA_CPU_TARGET=generic \
    USE_BINARYBUILDER=0 \
    USE_BINARYBUILDER_LLVM=0 \
    OPENBLAS_DYNAMIC_ARCH=0 \
    OPENBLAS_TARGET_ARCH=ARMV8 \
    USE_BLAS64=1 \
    BINARY=64 \
    LIBBLAS=-lopenblas \
    LIBBLASNAME=libopenblas \
    USE_SYSTEM_CSL=1 \
    USE_SYSTEM_LLVM=0 \
    USE_SYSTEM_LIBUNWIND=1 \
    USE_SYSTEM_PCRE=1 \
    USE_SYSTEM_LIBM=1 \
    USE_SYSTEM_OPENLIBM=1 \
    USE_SYSTEM_DSFMT=1 \
    USE_SYSTEM_LIBBLASTRAMPOLINE=1 \
    USE_SYSTEM_BLAS=1 \
    USE_SYSTEM_LAPACK=1 \
    USE_SYSTEM_GMP=1 \
    USE_SYSTEM_MPFR=1 \
    USE_SYSTEM_LIBSUITESPARSE=1 \
    USE_SYSTEM_LIBUV=1 \
    USE_SYSTEM_UTF8PROC=1 \
    USE_SYSTEM_MBEDTLS=1 \
    USE_SYSTEM_LIBSSH2=1 \
    USE_SYSTEM_NGHTTP2=1 \
    USE_SYSTEM_CURL=1 \
    USE_SYSTEM_LIBGIT2=1 \
    USE_SYSTEM_PATCHELF=1 \
    USE_SYSTEM_LIBWHICH=1 \
    USE_SYSTEM_ZLIB=1 \
    USE_SYSTEM_P7ZIP=1 \
    USE_SYSTEM_LLD=1 \
"

do_configure() {
  cp -a ${WORKDIR}/Make.user ${S}/Make.user
}

do_compile(){
  oe_runmake
}

do_install() {
  oe_runmake install
}
