pushd `dirname $0`

. ./util/progress.sh

chmod +x ./winetricks
./winetricks -q $INSTALL_PACKAGES

popd

