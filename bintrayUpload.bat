cd %cd%

gradlew clean build bintrayUpload -PbintrayUser=houxj2018 -PbintrayKey=c5f7a255378e0102f977e7585a93cc98966d19c8 -PdryRun=false

pause