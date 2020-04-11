# meta-bananapi

You can use gmacario/build-yocto(https://hub.docker.com/r/gmacario/build-yocto) docker image to build yocto
```shell
$ docker pull gmacario/build-yocto
$ docker run -it gmacario/build-yocto /bin/bash
```

How to build minimal SD image for Banana Pi BPI-W2
1. Clone yocto 
```shell
build@e8a32863a5dc:~$ git clone https://git.yoctoproject.org/git/poky -b sumo
```

2. Clone meta-banana
```shell
build@e8a32863a5dc:~$ cd poky
build@e8a32863a5dc:~/poky$
build@e8a32863a5dc:~/poky$ git clone https://github.com/JonasHuang178/meta-bananapi.git -b sumo
```

3. Run oe-init-build-env
```shell
build@e8a32863a5dc:~/poky$ . oe-init-build-env
```

4. Add meta-bananapi layer to $BUILDDIR/conf/bblayer.conf
```diff
@@ -9,4 +9,5 @@ BBLAYERS ?= " \
   /home/build/poky/meta \
   /home/build/poky/meta-poky \
   /home/build/poky/meta-yocto-bsp \
+  /home/build/poky/meta-bananapi \
   "
```

5. Modify machine type to bpi-w2-64 in $BUILDDIR/conf/local.conf

MACHINE ??= "bpi-w2-64"

6. build image
```shell
build@e8a32863a5dc:~/poky/build$ bitbake bpi-w2-basic-image
```

You can get a SD image 
```shell
build@e8a32863a5dc:~/poky/build$ ls $BUILDDIR/tmp/deploy/images/bpi-w2-64/bpi-w2-basic-image-bpi-w2-64.sdimg 
/home/build/poky/build/tmp/deploy/images/bpi-w2-64/bpi-w2-basic-image-bpi-w2-64.sdimg
```
