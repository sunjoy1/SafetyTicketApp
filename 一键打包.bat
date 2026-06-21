@echo off
chcp 65001 >nul
:: ============================================================
:: 安全管理作业票系统 - 一键打包脚本
:: ============================================================

set PROJECT_DIR=%~dp0

:: 使用 Android Studio 自带的 JDK
set JAVA_HOME=C:\Program Files\Android\Android Studio\jbr

:: Android SDK 路径（Android Studio 第一次启动后会自动创建）
set SDK_PATH=%LOCALAPPDATA%\Android\Sdk

set GRADLE=%PROJECT_DIR%gradlew.bat

if not exist "%GRADLE%" (
    echo [错误] 找不到 gradlew.bat
    pause
    exit /b 1
)

if not exist "%SDK_PATH%" (
    echo [提示] SDK 尚未下载
    echo 请先打开 Android Studio，它会自动引导你下载 SDK。
    echo 或者手动打开：Tools -> SDK Manager -> 安装 Android 34
    pause
    exit /b 1
)

echo ==========================================
echo   安全管理作业票系统 - 一键打包
echo ==========================================
echo.
echo 项目路径：%PROJECT_DIR%
echo SDK 路径：%SDK_PATH%
echo JDK 版本：
"%JAVA_HOME%\bin\java" -version 2>&1
echo.

:: 设置环境变量
set ANDROID_HOME=%SDK_PATH%
set ANDROID_SDK_ROOT=%SDK_PATH%
set PATH=%JAVA_HOME%\bin;%PATH%

cd /d "%PROJECT_DIR%"

echo [1/3] 正在编译项目，请耐心等待...
call "%GRADLE%" assembleDebug

if %ERRORLEVEL% neq 0 (
    echo.
    echo [错误] 编译失败
    echo 常见原因：
    echo   1. SDK 未下载完成 - 打开 Android Studio 检查 SDK Manager
    echo   2. 网络问题导致依赖下载失败 - 检查网络连接
    pause
    exit /b 1
)

echo.
echo [2/3] 编译成功！正在复制 APK 到桌面..

set APK_SOURCE=%PROJECT_DIR%app\build\outputs\apk\debug\app-debug.apk
set DESKTOP=%USERPROFILE%\Desktop\安全管理作业票系统.apk

copy /Y "%APK_SOURCE%" "%DESKTOP%" >nul

echo.
echo [3/3] 打包完成！
echo ==========================================
echo APK 已生成：
echo   %APK_SOURCE%
echo.
echo 已复制到桌面：
echo   %DESKTOP%
echo ==========================================
echo.
pause
