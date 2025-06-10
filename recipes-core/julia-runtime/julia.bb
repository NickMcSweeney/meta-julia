# meta-julia/recipes-julia/julia_runtime.bb
DESCRIPTION = "The Julia programming language"
HOMEPAGE = "https://julialang.org/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=172fb32b02b335508e62c04972eaad3f"

PV = "1.0"
MINOR_VERSION = "0"

SRC_URI = "https://julialang-s3.julialang.org/bin/linux/${@map_short_arch(d)}/${PV}/julia-${PV}.${MINOR_VERSION}-linux-${@map_arch(d)}.tar.gz;downloadfilename=${BP}.tar.gz"
SRC_URI[sha256sum] = "${@map_checksum(d)}"

S = "${WORKDIR}/julia-${PV}.${MINOR_VERSION}"

INSANE_SKIP:${PN} = "ldflags"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
SOLIBS = ".so*"
SOLIBSDEV = ".so.*"
FILES_SOLIBSDEV = ""

juliadir = "/opt/julia"

def map_arch(d):
    import re
    arch = d.getVar('TARGET_ARCH', True)
    if   re.match('i.86$', arch):    return 'i686'
    elif re.match('x86_64$', arch):  return 'x86_64'
    elif re.match('aarch32$', arch): return 'aarch64' # not supported at the moment - should be aarch32.. maybe this works?
    elif re.match('aarch64$', arch): return 'aarch64'
    return 'aarch64'

def map_short_arch(d):
    import re
    arch = d.getVar('TARGET_ARCH', True)
    if   re.match('i.86$', arch):    return 'x86'
    elif re.match('x86_64$', arch):  return 'x64'
    elif re.match('aarch32$', arch): return 'aarch64' # not supported at the moment - should be aarch32.. maybe this works?
    elif re.match('aarch64$', arch): return 'aarch64'
    return 'aarch64'

def map_bits(d):
    import re
    arch = d.getVar('TARGET_ARCH', True)
    if   re.match('i.86$', arch):    return 32
    elif re.match('x86_64$', arch):  return 64
    elif re.match('aarch32$', arch): return 32
    elif re.match('aarch64$', arch): return 64
    return 64

def map_checksum(d):
    import re
    arch = d.getVar('TARGET_ARCH', True)
    if   re.match('i.86$', arch):    return "c2b6f5763edd994cb01407b8e71797bd25901cc513132d155953742f54b3a294"
    elif re.match('x86_64$', arch):  return "723e878c642220cc0251a0e13758c059a389cadc7f01376feaf1ea7388fe8f9c"
    elif re.match('aarch32$', arch): return "f63203574fba25c9bda5e98b2f87f985d4672109855633a46bae32bfba3cbc0d"
    elif re.match('aarch64$', arch): return "f63203574fba25c9bda5e98b2f87f985d4672109855633a46bae32bfba3cbc0d"
    return "f63203574fba25c9bda5e98b2f87f985d4672109855633a46bae32bfba3cbc0d"

install_dir() {
    dir_name="$1"
    install -d "${D}${juliadir}/${dir_name}"
    for file in "${S}/${dir_name}"/*; do
        if [ -f "${file}" ]; then
            install -m 0644 -o root -g root "${file}" "${D}${juliadir}/${dir_name}/"
        elif [ -d "${file}" ]; then
            subdir="$(basename "${file}")"
            install_dir "${dir_name}/${subdir}"
        fi
    done
}

do_install(){
    install -d ${D}${juliadir}
    install -m 755 ${S}/LICENSE.md ${D}${juliadir}/LICENSE.md

    install_dir "bin"
    install_dir "etc"
    install_dir "include"
    install_dir "lib"
    install_dir "libexec"
    install_dir "share"

    install -d ${D}${bindir}
    ln -sr ${D}${juliadir}/bin/julia ${D}${bindir}/julia
}

FILES:${PN} = "${bindir}/julia \
               ${juliadir}/LICENSE.md \
               ${juliadir}/bin \
               ${juliadir}/etc \
               ${juliadir}/include \
               ${juliadir}/lib \
               ${juliadir}/libexec \
               ${juliadir}/share \
              "
