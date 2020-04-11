DESCRIPTION = "Banana Pi BPI-W2 binary files."
HOMEPAGE = "https://github.com/BPI-SINOVOIP/BPI-W2-bsp"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit deploy bin_package

SRC_URI = "git://github.com/BPI-SINOVOIP/BPI-W2-bsp.git;branch=master;protocol=http;subpath=rtk-pack/rtk/bpi-w2"
SRCREV = "6e6aefc35dc50b1b8231cdb03a995d088f29eb21"
PV = "${SRCREV}"
PR = "r0"

S = "${WORKDIR}/bpi-w2"

do_deploy() {
    install -d ${DEPLOYDIR}/${PN}

    install -m 755 ${S}/configs/default/linux/bluecore.audio ${DEPLOYDIR}/${PN}/bluecore-${PV}-${PR}.audio
    install -m 755 ${S}/configs/default/linux/uEnv.txt ${DEPLOYDIR}/${PN}/uEnv-${PV}-${PR}.txt
    install -m 755 ${S}/configs/default/linux/uInitrd ${DEPLOYDIR}/${PN}/uInitrd-${PV}-${PR}
    install -m 755 ${S}/bin/spirom-bpi-w2.bin ${DEPLOYDIR}/${PN}/spirom-bpi-w2-${PV}-${PR}.bin


    cd ${DEPLOYDIR}/${PN}
    rm -f bluecore.audio uEnv.txt uInitrd spirom-bpi-w2.bin

    ln -sf bluecore-${PV}-${PR}.audio bluecore.audio
    ln -sf uEnv-${PV}-${PR}.txt uEnv.txt
    ln -sf uInitrd-${PV}-${PR} uInitrd
    ln -sf spirom-bpi-w2-${PV}-${PR}.bin spirom-bpi-w2.bin
}

addtask deploy before do_build after do_install
do_deploy[dirs] += "${DEPLOYDIR}/${PN}"
