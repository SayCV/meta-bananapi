DESCRIPTION = "tools for Banana Pi"
HOMEPAGE = "https://github.com/BPI-SINOVOIP/bpi-tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
RDEPENDS_${PN} += "bash"

inherit bin_package

SRC_URI = "git://github.com/BPI-SINOVOIP/bpi-tools.git;protocol=https;branch=master"
SRCREV = "17f74bc7b5abefd053f3ab9fc1863a5537bb16c6"
PV = "${SRCPV}"
PR = "r0"

S = "${WORKDIR}/git"

bin_package_do_install() {
    bindir="/usr/bin"
    install -d ${D}/${bindir}

    install -m 755 ${S}/bpi-bootsel ${D}/${bindir}
    install -m 755 ${S}/bpi-copy ${D}/${bindir}
    install -m 755 ${S}/bpi-get ${D}/${bindir}
    install -m 755 ${S}/bpi-hw ${D}/${bindir}
    install -m 755 ${S}/bpi-migrate ${D}/${bindir}
    install -m 755 ${S}/bpi-tools ${D}/${bindir}
    install -m 755 ${S}/bpi-update ${D}/${bindir}
}

