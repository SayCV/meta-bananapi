DESCRIPTION = "U-boot-rtk for Banana PI W2"
HOMEPAGE = "https://github.com/BPI-SINOVOIP/BPI-W2-bsp"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "bc"

require recipes-bsp/u-boot/u-boot.inc

SRC_URI = "git://github.com/BPI-SINOVOIP/BPI-W2-bsp.git;branch=master;protocol=http;subpath=u-boot-rtk            file://0001-Fix-make-issue.patch"
SRCREV = "6e6aefc35dc50b1b8231cdb03a995d088f29eb21"
PV = "${SRCREV}"
PR = "r0"

S = "${WORKDIR}/u-boot-rtk"
UBOOT_MACHINE = "rtd1296_sd_bananapi_defconfig"

B = "${S}"

do_compile () {
    oe_runmake rtd1296_sd_bananapi_defconfig
    oe_runmake all BUILD_BOOTCODE_ONLY=true
   
    dd if=/dev/zero of=u-boot.tmp bs=1M count=1
    dd if=u-boot.bin of=u-boot.tmp bs=1k seek=40
    dd if=u-boot.tmp of=BPI-W2-720P-2k.img bs=1k skip=2 count=1022 status=noxfer
    gzip BPI-W2-720P-2k.img

    rm -f u-boot.tmp
}

do_install () {
   cp ${B}/u-boot.bin ${D}
   cp ${B}/BPI-W2-720P-2k.img.gz ${D}
}

FILES_${PN} = "/"

do_deploy_append() {
    install -m 755 ${B}/BPI-W2-720P-2k.img.gz ${DEPLOYDIR}/BPI-W2-720P-2k-${PV}-${PR}.img.gz

    cd ${DEPLOYDIR}
    rm -f BPI-W2-720P-2k.img.gz

    ln -sf BPI-W2-720P-2k-${PV}-${PR}.img.gz BPI-W2-720P-2k.img.gz
}
