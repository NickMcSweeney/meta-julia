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
INSANE_SKIP:${PN} += "already-stripped ldflags dev-so"
INSANE_SKIP:${PN}-dev += "already-stripped ldflags"

# specify dependancies
DEPENDS += "libgcc gcc-runtime"
RDEPENDS:${PN} += "libgcc libstdc++"

# specify the order for packages to be created in
PACKAGES = "${PN}-dbg ${PN} ${PN}-doc ${PN}-dev"

# set the base_prefix
base_prefix = "/opt/julia"

do_install() {
    ## install base directories
    install -d ${D}${bindir} # /opt/julia/bin
    install -d ${D}${sysconfdir} # /opt/julia/etc
    install -d ${D}${includedir} # /opt/jula/include
    install -d ${D}${libdir} # /opt/julia/lib
    install -d ${D}${libdir}/${PN}
    install -d ${D}${libexecdir} # /opt/julia/libexec 
    install -d ${D}${datadir} # /usr/share
    install -d ${D}${docdir}
    install -d ${D}${mandir}
    #install -d ${D}${datadir}/${PN} # /usr/share/julia

    # install julia libs
    oe_soinstall ${S}/lib/julia/libamd.so.2.4.6 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libatomic.so.1.2.0 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libbtf.so.1.2.6 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libcamd.so.2.4.6 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libccolamd.so.2.9.6 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libcholmod.so.3.0.13 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libcolamd.so.2.9.6 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libcurl.so.4.7.0 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libgfortran.so.4.0.0 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libgit2.so.1.1.0 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libgmp.so.10.4.1 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libgmpxx.so.4.6.1 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libgomp.so.1.0.0 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libjulia-internal.so.1.6 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libklu.so.1.3.8 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libldl.so.2.2.6 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libmbedcrypto.so.2.24.0 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libmbedtls.so.2.24.0 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libmbedx509.so.2.24.0 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libmpfr.so.6.1.0 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libnghttp2.so.14.20.0 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libopenlibm.so.3.0 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libpcre2-8.so.0.11.0 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/librbio.so.2.2.6 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libspqr.so.2.0.9 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libssh2.so.1.0.1 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libssp.so.0.0.0 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libsuitesparseconfig.so.5.4.0 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libumfpack.so.5.7.8 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libunwind.so.8.0.1 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libuv.so.2.0.0 ${D}${libdir}/${PN}
    oe_soinstall ${S}/lib/julia/libz.so.1.2.12 ${D}${libdir}/${PN}

    # install versioned debug libs
    install -m 0755 ${S}/lib/julia/libccalltest.so.debug ${D}${libdir}/${PN}

    # install non-versioned libs
    install -m 0755 ${S}/lib/julia/sys.so ${D}${libdir}/${PN}
    install -m 0755 ${S}/lib/julia/libLLVM-11jl.so ${D}${libdir}/${PN}
    install -m 0755 ${S}/lib/julia/libdSFMT.so ${D}${libdir}/${PN}
    install -m 0755 ${S}/lib/julia/libccalltest.so ${D}${libdir}/${PN}
    install -m 0755 ${S}/lib/julia/libllvmcalltest.so ${D}${libdir}/${PN}
    install -m 0755 ${S}/lib/julia/libsuitesparse_wrapper.so ${D}${libdir}/${PN}

    # install the system libraries
    oe_libinstall libgcc ${D}${libdir}/${PN}
    oe_libinstall libstdc++ ${D}${libdir}/${PN}

    # install the julia library to the lib
    oe_soinstall ${S}/lib/libjulia.so.1.6 ${D}${libdir}

    # link OS packages to the julia lib dir
    ln -sr ${D}/lib/libgcc_s.so.1 ${D}${libdir}/${PN}/libgcc_s.so.1
    ln -sr ${D}/lib/libgcc_s.so ${D}${libdir}/${PN}/libgcc_s.so
    ln -sr ${D}/lib/libstdc++.so ${D}${libdir}/${PN}/libstdc++.so
    ln -sr ${D}/lib/libstdc++.so.6 ${D}${libdir}/${PN}/libstdc++.so.6

    # install openblas
    cp --no-dereference --preserve=mode,links -v ${S}/lib/julia/libopenblas.0.3.10.so ${S}/lib/julia/libopenblas.so.0.3.10
    oe_soinstall ${S}/lib/julia/libopenblas.so.0.3.10 ${D}${libdir}/${PN}

    # link julia packages in julia lib
    ln -sr ${D}/lib/${PN}/libLLVM-11jl.so ${D}${libdir}/${PN}/libLLVM.so

    # install internal system application
    install -m 0755 ${S}/libexec/7z ${D}${libexecdir}/

    # install configuration file
    install -m 0755 ${S}/etc/julia/startup.jl ${D}${sysconfdir}/startup.jl

    # copy data dirs for julia
    cp -R --no-dereference --preserve=mode,links -v ${S}/share/julia ${D}${datadir}/julia
    cp -R --no-dereference --preserve=mode,links -v ${S}/share/doc/julia ${D}${docdir}/julia
    # install data files for julia
    cp -R --no-dereference --preserve=mode,links -v ${S}/share/appdata ${D}${datadir}/appdata
    cp -R --no-dereference --preserve=mode,links -v ${S}/share/applications ${D}${datadir}/applications
    cp -R --no-dereference --preserve=mode,links -v ${S}/share/man/man1/* ${D}${mandir}/

    # copy over the included julia files
    cp -R --no-dereference --preserve=mode,links -v ${S}/include/julia ${D}${includedir}/julia

    # install the binary for julia
    install -m 0755 ${S}/bin/julia ${D}${bindir}

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
