pushd `dirname $0`

. ./util/download.sh
. ./util/cabextract.sh
. ./util/progress.sh
. ./hosts/hosts.sh
. ./package/corefonts.sh

TMPDIR=`mktemp -d`

pushd $TMPDIR
install_corefonts
popd
popd
rm -rf ${TMPDIR}
progress "-1" "Launching application..."
eval "$@"



