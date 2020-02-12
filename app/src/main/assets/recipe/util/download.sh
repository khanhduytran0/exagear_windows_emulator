function recipe_download() {
    URL=$1
    NAME=$2
    wget --no-check-certificate $URL -O $NAME
}
