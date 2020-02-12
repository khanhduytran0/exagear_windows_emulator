pushd `dirname $0`

. ./util/download.sh
. ./package/corefonts_nocabextract.sh
. ./hosts/hosts.sh

TMPDIR=`mktemp -d`

pushd $TMPDIR
install_corefonts_nocabextract
popd
popd
rm -rf ${TMPDIR}
eval "$@"

