# Base this image on core-image-minimal
include recipes-core/images/core-image-minimal.bb

IMAGE_FSTYPES += "ext3 sdimg"

# image rootfs size 1GB
IMAGE_ROOTFS_SIZE = "1000000"
