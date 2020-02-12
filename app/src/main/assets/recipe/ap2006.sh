pushd `dirname $0`

. ./util/progress.sh

chmod +x ./winetricks
./winetricks -q tahoma jet40

popd

progress "-1" "Generating locale..."

locale-gen --no-archive he_IL.utf8
rm /usr/share/wine/fonts/ssee1255.fon

progress "-1" "Launching application..."

cp `dirname $0`/ap2006.bat .
set -- "${@:1:$(($#-1))}"
LC_ALL=he_IL.utf8 wine "$@" ./ap2006.bat

