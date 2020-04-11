inherit image_types
# Create an image that can be written onto a SD card using dd.
# copy from meta-raspberrypi

#                     |  First partition |  Second partition  |  
#  ------------------------------------------------------------
# |    IMAGE_UBOOT    |    IMAGE_BOOT    |    ROOTFS_SIZE     |
#  ------------------------------------------------------------
# ^                   ^            ^                          ^
# |                   |            |                          |
# 0                  2048     2048 + 64MB         2048 + 64Mb + SDIMG_ROOTFS

# This image depends on the rootfs image
IMAGE_TYPEDEP_sdimg = "${SDIMG_ROOTFS_TYPE}"

# Boot file package
BPI-w2_BOOT ?= "boot-files"

# Boot partition volume id
BOOTDD_VOLUME_ID ?= "${MACHINE}"

# U-boot partition size 
IMAGE_UBOOT ?= "2048"

# boot partition size
IMAGE_BOOT ?= "64"

# Use an uncompressed ext3 by default as rootfs
SDIMG_ROOTFS_TYPE ?= "ext3"
SDIMG_ROOTFS = "${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.${SDIMG_ROOTFS_TYPE}"

do_image_sdimg[depends] = " \
    parted-native:do_populate_sysroot \
    mtools-native:do_populate_sysroot \
    dosfstools-native:do_populate_sysroot \
    u-boot-rtk:do_deploy \
    ${BPI-w2_BOOT}:do_deploy \
"

#do_image_sdimg[recrdeps] = "do_build"

# SD card image name
SDIMG = "${IMGDEPLOYDIR}/${IMAGE_NAME}.rootfs.sdimg"

IMAGE_CMD_sdimg () {

    # Align partition
    BOOT_ALIGN=$(expr ${IMAGE_UBOOT} / 2)
    ROOT_ALIGN=$(expr ${IMAGE_BOOT} \* 1024 + ${IMAGE_UBOOT})
    SDIMG_SIZE=$(expr ${BOOT_ALIGN} + ${ROOT_ALIGN} + ${ROOTFS_SIZE})

    # Initialize sdcard image file
    dd if=/dev/zero of=${SDIMG} bs=1024 count=0 seek=${SDIMG_SIZE}

    # Create partition table
    parted -s ${SDIMG} mklabel msdos
    # Create boot partition and mark it as bootable
    parted -s ${SDIMG} unit KiB mkpart primary fat32 ${BOOT_ALIGN} ${ROOT_ALIGN}
    parted -s ${SDIMG} set 1 boot on
    # Create rootfs partition to the end of disk
    parted -s ${SDIMG} -- unit KiB mkpart primary ext3 ${ROOT_ALIGN} -1s
    parted ${SDIMG} print

    # Create a vfat image
    BOOT_BLOCKS=$(LC_ALL=C parted -s ${SDIMG} unit b print | awk '/ 1 / { print substr($4, 1, length($4 -1)) / 512 /2 }')
    rm -f ${WORKDIR}/boot.img
    mkfs.vfat -n "${BOOTDD_VOLUME_ID}" -S 512 -C ${WORKDIR}/boot.img $BOOT_BLOCKS
    # Copy boot files
    mkdir -p ${WORKDIR}/bananapi/bpi-w2/linux
    cp ${DEPLOY_DIR_IMAGE}/${BPI-w2_BOOT}/bluecore.audio ${WORKDIR}/bananapi/bpi-w2/linux
    cp ${DEPLOY_DIR_IMAGE}/${BPI-w2_BOOT}/uEnv.txt ${WORKDIR}/bananapi/bpi-w2/linux
    cp ${DEPLOY_DIR_IMAGE}/${BPI-w2_BOOT}/uInitrd ${WORKDIR}/bananapi/bpi-w2/linux
    cp ${DEPLOY_DIR_IMAGE}/${BPI-w2_BOOT}/spirom-bpi-w2.bin ${WORKDIR}/bananapi/bpi-w2/linux
    cp ${DEPLOY_DIR_IMAGE}/rtd-1296-bananapi-w2-2GB.dtb ${WORKDIR}/bananapi/bpi-w2/linux
    cp ${DEPLOY_DIR_IMAGE}/Image ${WORKDIR}/bananapi/bpi-w2/linux/uImage
    mcopy -i ${WORKDIR}/boot.img -s ${WORKDIR}/bananapi ::/

    # Burn Partitions
    # U-boot image
    gunzip -c ${DEPLOY_DIR_IMAGE}/BPI-W2-720P-2k.img.gz | dd of=${SDIMG} bs=1024 seek=2 conv=notrunc
    # BOOT Partition
    dd if=${WORKDIR}/boot.img of=${SDIMG} conv=notrunc seek=${BOOT_ALIGN} bs=1024
    # ROOT Partition
    dd if=${SDIMG_ROOTFS} of=${SDIMG} conv=notrunc seek=${ROOT_ALIGN} bs=1024
}

