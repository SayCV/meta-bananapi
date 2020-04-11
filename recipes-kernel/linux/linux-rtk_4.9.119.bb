DESCRIPTION = "linux-rtk for Banana PI W2"
HOMEPAGE = "https://github.com/BPI-SINOVOIP/BPI-W2-bsp"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel siteinfo
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION = "4.9.119"
PV = "${LINUX_VERSION}"
PR = "r0"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}_${PV}:"
SRC_URI = "git://github.com/BPI-SINOVOIP/BPI-W2-bsp.git;branch=master;protocol=http;destsuffix=BPI-W2-bsp\
           file://defconfig \
          "
SRCREV = "c6e3415d4d4b7f61e1f7e1f6a925b6af521a09df"

KCONFIG_MODE = "--alldefconfig"

# Add the kernel debugger over console kernel command line option if enabled
CMDLINE_append = ' ${@oe.utils.conditional("ENABLE_KGDB", "1", "kgdboc=serial0,115200", "", d)}'

# You can define CMDLINE_DEBUG as "debug" in your local.conf or distro.conf
# to enable kernel debugging.
CMDLINE_DEBUG ?= ""
CMDLINE_append = " ${CMDLINE_DEBUG}"

KBUILD_DEFCONFIG ?= "rtd129x_bpi_defconfig"
KERNEL_DEVICETREE ?= "realtek/rtd129x/rtd-1296-bananapi-w2-2GB.dtb"

S = "${WORKDIR}/BPI-W2-bsp/linux-rtk/"
