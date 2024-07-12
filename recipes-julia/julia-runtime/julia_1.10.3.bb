# meta-julia/recipes-julia/julia/julia_1.10.3.bb
DESCRIPTION = "The Julia programming language"
HOMEPAGE = "https://julialang.org/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=<checksum>"
MAJOR_MINOR = "1.10"

SRC_URI = "https://julialang-s3.julialang.org/bin/linux/aarch64/${MAJOR_MINOR}/julia-${PV}-linux-aarch64.tar.gz"
SRC_URI[md5sum] = "6a40929ec458f116195f09b9e3c8b054"
SRC_URI[sha256sum] = "2d52a61826872b3170c65f99a954bd9d21a31211cb50948056d924f811a0024f"

S = "${WORKDIR}/julia-${PV}"

inherit cmake

# Julia dependencies
DEPENDS = "libuv openssl pcre2 mbedtls zlib"

# Custom installation steps, if any
do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/julia ${D}${bindir}/julia
}

FILES_${PN} += "${bindir}/julia"
