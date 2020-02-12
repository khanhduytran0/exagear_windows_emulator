function do_cabextract() {
    CABFILE=$1
    DIR=$2
    cabextract -q -d ${DIR} ${CABFILE}
}
