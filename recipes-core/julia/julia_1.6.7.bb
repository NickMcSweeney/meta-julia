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
INSANE_SKIP_${PN} += "already-stripped ldflags dev-so"
INSANE_SKIP_${PN}-dev += "already-stripped ldflags"

# specify dependancies
DEPENDS += "libgcc gcc-runtime"
RDEPENDS_${PN} += "libgcc libstdc++"

# specify the order for packages to be created in
PACKAGES = "${PN}-dbg ${PN} ${PN}-doc ${PN}-dev"

base_prefic = "/opt/${PN}"

do_install() { 
    # Install `includedir` as `/opt/julia/include`
    install -d ${D}${includedir}
    ## install julia headers
    cp -R --no-dereference --preserve=mode,links -v ${S}/include/julia/* ${D}${includedir}/

    # Install `datadir` as `/usr/share`
    install -d ${D}${datadir} # /usr/share
    ## install julia data files
    cp -R --no-dereference --preserve=mode,links -v ${S}/share/* ${D}${datadir}/
     
    # Install `sysconfdir` as `/opt/julia/etc`
    install -d ${D}${sysconfdir}
    ## install system configs
    install -m 0755 ${S}/etc/julia/* ${D}${sysconfdir}/
    
    # Install `libexecdir` as `/usr/libexec` 
    install -d ${D}${libexecdir}
    ## install internal application libraries
    install -m 0755 ${S}/libexec/7z ${D}${libexecdir}/7z
    
    # Install `libdir` as `/usr/lib`
    install -d ${D}${libdir}
    ## install external application libraries
    oe_libinstall libgcc ${D}${libdir}/${PN}
    oe_libinstall libstdc++ ${D}${libdir}/${PN}
    ### link OS packages to the julia lib dir
    lnr ${D}/lib/libgcc_s.so.1 ${D}${libdir}/${PN}/libgcc_s.so.1
    lnr ${D}/lib/libgcc_s.so ${D}${libdir}/${PN}/libgcc_s.so
    lnr ${D}/lib/libstdc++.so ${D}${libdir}/${PN}/libstdc++.so
    lnr ${D}/lib/libstdc++.so.6 ${D}${libdir}/${PN}/libstdc++.so.6

    # Install `libdir/PN` libs as `/usr/lib/julia`
    install -d ${D}${libdir}/${PN}
    ## install julia application libraries
    ### lookup libs
    install -d -m 0755 ${WORKDIR}/${PN}lib
    find ${S}/lib/${PN} -type f -name *.so* -exec cp -R --no-dereference --preserve=mode,links -v {} ${WORKDIR}/${PN}lib \;
    ### install non-versioned libs
    install -m 0755 ${WORKDIR}/${PN}lib/*.so ${D}${libdir}/${PN}
    ### install versioned libs
    so_oeinstall ${WORKDIR}/${PN}lib/*.so.* ${D}${libdir}/${PN}
    # TODO: does this need to be 'cleaned up' rm -rf ${WORKDIR}/${PN}lib
    
    ## install julia linked libraries
    ### lookup libs
    install -d -m 0755 ${WORKDIR}/${PN}lib-dev
    find ${S}/lib/${PN} -type l -name *.so* -exec cp -R --no-dereference --preserve=mode,links -v {} ${WORKDIR}/${PN}lib \;
    ### install linked libs
    cp -R --no-dereference --preserve=mode,links -v ${WORKDIR}/${PN}lib-dev/* ${D}${libdir}/${PN}
    # TODO: does this need to be 'cleaned up' rm -rf ${WORKDIR}/${PN}lib-dev
    
    # TODO: does this still need to be done?
    ## handle special cases
    ### install openblas
    #cp --no-dereference --preserve=mode,links -v ${S}/lib/julia/libopenblas.0.3.10.so ${S}/lib/julia/libopenblas.so.0.3.10
    #oe_soinstall ${S}/lib/julia/libopenblas.so.0.3.10 ${D}${libdir}/${PN}
    ### link julia packages in julia lib
    #lnr ${D}/lib/${PN}/libLLVM-11jl.so ${D}${libdir}/${PN}/libLLVM.so
    
    ## install julia libraries
    install -m 0755 ${S}/lib/libjulia.so ${D}${libdir}
    install -m 0755 ${S}/lib/libjulia.so.1 ${D}${libdir}
    so_oeinstall ${S}/lib/libjulia.so.1.6 ${D}${libdir}

    # Install `bindir` as `/usr/bin`
    install -d ${D}${bindir}
    ## install julia binary
    install -m 0755 ${S}/bin/julia ${D}${bindir}
    
    # Install `/etc/profile.d` for julia system configuration
    install -d ${D}/etc/profile.d
    ## create profile file and set important variables
    echo "export JULIA_BINDIR='/usr/bin'" > ${WORKDIR}/julia.sh
    echo "export JULIA_DEPOT_PATH='/var/opt/julia'" >> ${WORKDIR}/julia.sh
    # install julia profile
    install -m 0755 ${WORKDIR}/julia.sh ${D}/etc/profile.d/julia.sh
}

# specifiy the files for each package
## Debug Packages
FILES_${PN}-dbg += ""
## Base Packages
FILES_${PN} += "${includedir} \
        ${datadir} \
        ${sysconfdir} \
        ${libexecdir} \
        ${libdir} \
        ${bindir} \
        /etc/profile.d"
## Docs Packages
FILES_${PN}-doc += ""
## Development Packages
FILES_${PN}-dev += ""