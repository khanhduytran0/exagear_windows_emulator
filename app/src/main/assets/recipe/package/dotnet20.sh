function install_dotnet20 {
    recipe_download ${DOTNET40_DOWNLOAD_HOST}/dotnetfx.exe dotnetfx.exe
    wine ./dotnetfx.exe
}
