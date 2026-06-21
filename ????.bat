@echo off
chcp 65001 >nul
:: ============================================================
:: 环境检查脚本 - 运行前确认打包条件
:: ============================================================

echo ==========================================
echo   环境检查
echo ==========================================

call :check "Java JDK" "java" "javac -version"
call :check "Android SDK" "adb" "adb --version"
call :check "Gradle Wrapper" "gradlew.bat" "gradlew.bat --version" "请确认在项目根目录运行"

echo.
echo ==========================================
echo 检查完成。如果全部通过，请运行 "一键打包.bat" 进行打包。
echo ==========================================
pause
exit /b

:check
set "NAME=%~1"
set "CMD=%~2"
set "VERCMD=%~3"
set "EXTRA=%~4"

%CMD% >nul 2>nul
if %ERRORLEVEL% == 0 (
    echo [✓] %NAME% 已安装
    %VERCMD% 2>nul | findstr /V "^$" | head -1
) else (
    echo [✗] %NAME% 未找到！
    if not "%EXTRA%"=="" echo     %EXTRA%
)
echo.
exit /b
