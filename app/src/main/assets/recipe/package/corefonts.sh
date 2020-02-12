ALL_FONTS='arial32.exe arialb32.exe comic32.exe courie32.exe georgi32.exe impact32.exe times32.exe trebuc32.exe verdan32.exe webdin32.exe'

FONTSDIR=${WINEPREFIX}/drive_c/windows/Fonts/
WINTEMPDIR=${WINEPREFIX}/drive_c/windows/temp/corefonts
WINWINTEMPDIR='C:\windows\temp\corefonts'

function prepare()
{
    mkdir -p ${WINTEMPDIR}
    cat > ${WINTEMPDIR}/_register-fonts.reg <<_EOF_
REGEDIT4

_EOF_
}

function prepare_reg_font()
{
    FILE=$1
    FONT=$2

    case "$FILE" in
    *.TTF|*.ttf) FONT="$FONT (TrueType)";;
    esac

    cat >> ${WINTEMPDIR}/_register-fonts.reg <<_EOF_
[HKEY_LOCAL_MACHINE\Software\Microsoft\Windows NT\CurrentVersion\Fonts]
"$FONT"="$FILE"

[HKEY_LOCAL_MACHINE\Software\Microsoft\Windows\CurrentVersion\Fonts]
"FONT"="$FONT"

_EOF_
}

function do_reg_fonts()
{
    wine regedit "${WINWINTEMPDIR}\\_register-fonts.reg"
}

function install_corefonts() {
    FONTS_COUNT=0;

    for EXENAME in ${ALL_FONTS}
    do
        ((FONTS_COUNT++))
    done
    STEP=$((100/FONTS_COUNT))

    CUR_STEP=0
    for EXENAME in ${ALL_FONTS}
    do
    progress $CUR_STEP "Downloading fonts..."
	recipe_download ${COREFONTS_DOWNLOAD_HOST}/${EXENAME} ${EXENAME}
    ((CUR_STEP+=STEP))
    done

    CUR_STEP=0
    for EXENAME in ${ALL_FONTS}
    do
        progress $CUR_STEP "Extracting fonts..."
        mkdir ${EXENAME}_cab
        do_cabextract ${EXENAME} ${EXENAME}_cab
        cp -f ${EXENAME}_cab/*.ttf ${FONTSDIR}
        cp -f ${EXENAME}_cab/*.TTF ${FONTSDIR}
        ((CUR_STEP+=STEP))
    done

    progress "-1" "Registering fonts..."

    prepare

    prepare_reg_font Arial.TTF "Arial"
    prepare_reg_font Arialbd.TTF "Arial Bold"
    prepare_reg_font Arialbi.TTF "Arial Bold Italic"
    prepare_reg_font Ariali.TTF "Arial Italic"
    prepare_reg_font AriBlk.TTF "Arial Black"

    prepare_reg_font Comic.TTF "Comic Sans MS"
    prepare_reg_font Comicbd.TTF "Comic Sans MS Bold"

    prepare_reg_font Cour.TTF "Courier New"
    prepare_reg_font CourBD.TTF "Courier New Bold"
    prepare_reg_font CourBI.TTF "Courier New Bold Italic"
    prepare_reg_font Couri.TTF "Courier New Italic"

    prepare_reg_font Georgia.TTF "Georgia"
    prepare_reg_font Georgiab.TTF "Georgia Bold"
    prepare_reg_font Georgiaz.TTF "Georgia Bold Italic"
    prepare_reg_font Georgiai.TTF "Georgia Italic"

    prepare_reg_font Impact.TTF "Impact"

    prepare_reg_font Times.TTF "Times New Roman"
    prepare_reg_font Timesbd.TTF "Times New Roman Bold"
    prepare_reg_font Timesbi.TTF "Times New Roman Bold Italic"
    prepare_reg_font Timesi.TTF "Times New Roman Italic"

    prepare_reg_font Trebuc.TTF "Trebucet MS"
    prepare_reg_font Trebucbd.TTF "Trebucet MS Bold"
    prepare_reg_font Trebucbi.TTF "Trebucet MS Bold Italic"
    prepare_reg_font Trebucit.TTF "Trebucet MS Italic"

    prepare_reg_font Verdana.TTF "Verdana"
    prepare_reg_font Verdanab.TTF "Verdana Bold"
    prepare_reg_font Verdanaz.TTF "Verdana Bold Italic"
    prepare_reg_font Verdanai.TTF "Verdana Italic"

    do_reg_fonts

    progress "-1" ""
}

