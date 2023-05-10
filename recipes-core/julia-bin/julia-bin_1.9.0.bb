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

JULIA_VERSION="1.9.0"
JULIA_ARCH="aarch64"
JULIA_RELEASE="1.9" 

# Yocto will download and extract the uri. the sha256sum is required to validate.
SRC_URI = "https://julialang-s3.julialang.org/bin/linux/${JULIA_ARCH}/${JULIA_RELEASE}/julia-${JULIA_VERSION}-linux-${JULIA_ARCH}.tar.gz"
SRC_URI[sha256sum] = "0a14315b53acd97f22d26d4a8fd2c237e524e95c3bec98d2d78b54d80c2bc364"

# set the base_prefix
base_prefix = "/opt"

do_install() {
    ## install base directories
    install -d ${D}${base_prefix} # /opt
    # copy over the julia files
    cp -R --no-dereference --preserve=mode,links -v ${S} ${D}${base_prefix}/${PN}

    install -d ${D}${bindir} # /usr/bin
    # install the binary for julia
    ln -sr ${D}${base_prefix}/${PN}/bin/julia ${D}${bindir}/julia

    # Install `/etc/profile.d` for julia system configuration
    install -d ${D}/etc/profile.d
    ## create profile file and set important variables
    echo "export JULIA_BINDIR='/usr/bin'" > ${WORKDIR}/${PN}.sh
    echo "export JULIA_DEPOT_PATH='/var/opt/${PN}'" >> ${WORKDIR}/${PN}.sh
    # install julia profile
    install -Dm 0755 ${WORKDIR}/${PN}.sh ${D}/etc/profile.d/
}

FILES:${PN} += "/etc/profile.d/${PN}.sh ${base_prefix}/${PN}"