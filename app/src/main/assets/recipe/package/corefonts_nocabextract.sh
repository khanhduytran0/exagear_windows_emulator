ALL_FONTS='arial32.exe arialb32.exe comic32.exe courie32.exe georgi32.exe impact32.exe times32.exe trebuc32.exe verdan32.exe webdin32.exe'
function install_corefonts_nocabextract() {
    for EXENAME in ${ALL_FONTS}
    do
        recipe_download ${COREFONTS_DOWNLOAD_HOST}/${EXENAME} ${EXENAME}
    done

    for EXENAME in ${ALL_FONTS}
    do
        wine ./${EXENAME}
    done
}
