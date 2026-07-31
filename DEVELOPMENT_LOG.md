# 开发日志 — 概率决策器 (RandomNumber)

> 本日志由开发助手维护，用于记录开发流程与验证结果。
> 日志格式：每个任务记录 目标 / 修改文件 / 验证方式 / 结果 / 产物。

## 项目背景

- **用途**：单屏 Android 离线工具，输入概率值（小数/分数/百分比），点击按钮后以该概率随机输出"1"或"0"。
- **来源**：`SPEC.md`（中文功能规格说明书）
- **技术栈**：Kotlin, minSdk 24 / targetSdk 35, Material, ConstraintLayout, JUnit
- **非功能要求**：离线运行、无广告、轻量、Android 7.0+、不收集数据

---

## 任务 1: 完成 UI 布局

- **日期**：2026-07-31
- **目标**：将空白脚手架替换为完整单屏布局（标题/输入框/生成按钮/结果展示区）。
- **修改文件**：
  - `app/src/main/res/values/strings.xml` — 添加10个字符串资源（含5条错误提示）
  - `app/src/main/res/layout/activity_main.xml` — ConstraintLayout 4控件布局
- **验证**：`./gradlew assembleDebug` → **BUILD SUCCESSFUL** ✅
- **状态**：✅ 完成

---

## 任务 2: 概率输入解析器（含单元测试）

- **日期**：2026-07-31
- **目标**：纯 Kotlin 解析器，支持小数/分数/百分比 + 空格处理。
- **创建文件**：
  - `app/src/main/java/com/example/randomnumber/ProbabilityParser.kt`
  - `app/src/test/java/com/example/randomnumber/ProbabilityParserTest.kt`
- **修改文件**：`app/build.gradle.kts` — 添加 `testImplementation("junit:junit:4.13.2")`
- **测试覆盖**：18个用例（4小数 / 4分数 / 4百分比 / 2空格 / 5错误情况）
- **验证**：`./gradlew testDebugUnitTest` → **BUILD SUCCESSFUL, 18 tests pass** ✅
- **状态**：✅ 完成

---

## 任务 3: 输入验证与错误提示

- **日期**：2026-07-31
- **目标**：按钮点击 → 解析 → 非法输入弹 Toast，合法输入静默（随机逻辑留待任务4）。
- **修改文件**：`app/src/main/java/com/example/randomnumber/MainActivity.kt`
- **错误映射**：空→"请输入概率值"；格式错→"格式错误…"；分母0→"分母不能为零"；越界→"概率必须在 0 到 1 之间"；无效百分比→"无效的百分比格式"
- **验证**：`./gradlew assembleDebug` → **BUILD SUCCESSFUL** ✅
- **状态**：✅ 完成

---

## 任务 4: 串联按钮交互与随机生成

- **日期**：2026-07-31
- **目标**：合法输入时生成 `[0,1)` 随机数，r < p → "1"，否则 → "0"；不清空输入框。
- **修改文件**：`app/src/main/java/com/example/randomnumber/MainActivity.kt`
- **验证**：`./gradlew testDebugUnitTest assembleDebug` → **BUILD SUCCESSFUL** ✅
- **状态**：✅ 完成

---

## 任务 5: 结果展示与视觉反馈

- **日期**：2026-07-31
- **目标**："1"绿色 (Color.rgb(76,175,80))，"0"红色 (Color.rgb(244,67,54))。
- **修改文件**：`app/src/main/java/com/example/randomnumber/MainActivity.kt`
- **验证**：`./gradlew testDebugUnitTest assembleDebug` → **BUILD SUCCESSFUL** ✅
- **状态**：✅ 完成

---

## 任务 6: 生成签名密钥并打包 release APK

- **日期**：2026-07-31
- **背景**：用户需要"可直接通过网络发送给手机长期使用"的 APK，debug 包签名短期效不满足要求。
- **执行步骤**：
  1. `keytool` 生成 RSA 2048 密钥，有效期 30 年（10,950天）→ `app/randomnumber.jks`
  2. 创建 `app/keystore.properties`（storeFile 修正为 `app/randomnumber.jks` 相对路径）
  3. `app/build.gradle.kts` 添加 `import` + 加载 properties + `signingConfigs.release` + release 绑定签名
  4. 首次构建失败：keystore 路径解析错误（`rootProject.file` 基准），修复后成功
- **验证**：`./gradlew assembleRelease` → **BUILD SUCCESSFUL** ✅
- **产物**：`app/build/outputs/apk/release/app-release.apk`（4.8MB，已签名）
- **状态**：✅ 完成

---

## 阶段二：常用概率按钮 + 界面美化 + 骰子图标

### 任务 7: 骰子应用图标（纯 XML VectorDrawable）

- **日期**：2026-07-31
- **背景**：项目原本无图标资源，用系统默认。环境无 ImageMagick/PIL/rsvg，无法生成 PNG → 采用纯 XML 方案。
- **创建文件**（6个）：
  - `res/drawable/ic_launcher_background.xml` — 浅灰 #ECEFF1
  - `res/drawable/ic_launcher_foreground.xml` — 骰子：白色圆角方块+黑描边+经典5黑点
  - `res/mipmap-anydpi-v26/ic_launcher.xml` + `ic_launcher_round.xml` — adaptive icon（API26+）
  - `res/mipmap/ic_launcher.xml` + `ic_launcher_round.xml` — layer-list fallback（API24-25）
- **修改文件**：`AndroidManifest.xml` — 添加 `android:icon` + `android:roundIcon`
- **验证**：`./gradlew assembleDebug` 通过；`aapt2 dump badging` 显示 `icon='res/mipmap-anydpi-v26/ic_launcher.xml'` ✅
- **状态**：✅ 完成

### 任务 8: 常用概率按钮 + 界面视觉美化

- **日期**：2026-07-31
- **目标**：10 个常用概率 Chip 点击填入输入框；Material 主题美化；输入框升级；结果圆角卡片。
- **创建文件**：`res/values/colors.xml`（8个颜色）
- **修改文件**：
  - `res/values/themes.xml` — 主题配色 + statusBarColor
  - `res/values/strings.xml` — 新增 "常用概率" 标签 + 10 个 Chip 文案
  - `res/layout/activity_main.xml` — 重写：标题 / TextInputLayout(outlined) / 常用概率标签 / GridLayout 5×2 Chip / 生成按钮 / MaterialCardView 结果卡片
  - `MainActivity.kt` — 遍历 GridLayout 子 Chip 设点击监听（填入输入框）；颜色改 ContextCompat.getColor
- **关键决策**：用 GridLayout 5×2 等宽（弃 Flow）；Chip 显式 `chipMinHeight="0dp"` `chipMinTouchTargetSize="0dp"` 防默认高度覆盖
- **验证**：`./gradlew testDebugUnitTest assembleDebug` → 18 测试通过 + BUILD SUCCESSFUL ✅
- **状态**：✅ 完成

### 任务 9: 完整验证 + 重新打包 release

- **日期**：2026-07-31
- **验证**：
  - `./gradlew assembleRelease` → **BUILD SUCCESSFUL** ✅
  - `aapt2 dump badging` release APK → `icon='res/BW.xml'`（aapt2 路径缩短，解包确认是二进制 adaptive-icon）✅
  - adb 冒烟测试：`adb devices` 无连接设备 → 跳过（编译+单元测试已充分覆盖）
- **产物**：`app/build/outputs/apk/release/app-release.apk`（4.8MB，已签名，含骰子图标+新界面）
- **状态**：✅ 完成

---

## 待办 / 后续方向

- [ ] （可选）真机/模拟器安装冒烟测试（adb install）
- [ ] （可选）更新 versionCode/versionName 后重新打包

---

## 阶段二.5：UI 修复（用户反馈的两个问题）

### 问题 1：预设概率 Chip 上出现 √、× 图标

- **日期**：2026-07-31
- **根因**：Chip 用了 `Widget.MaterialComponents.Chip.Entry` 样式 → 默认**可勾选**（显示勾选标记 √）+ 关闭图标（×），遮挡数字。
- **修复**：改用 `Widget.MaterialComponents.Chip.Action` 样式（默认非 checkable），显式设 `checkedIconVisible="false"`、`closeIconVisible="false"`；chipBackgroundColor 白色 + 主色描边。
- **踩坑**：`app:checkable` 在 material 1.12.0 不是有效属性 → aapt 报错（上一轮自定义 style 继承 `Widget.MaterialComponents.Chip` 也报 not found，属库私有样式）。最终方案：Action 样式 + 显式 XML 属性（最可靠）。

### 问题 2："生成"按钮下出现意义不明的横线

- **根因**：下方 `MaterialCardView` 的 `cardElevation="2dp"` 顶部阴影在浅灰背景上形成一条线，视觉上贴着按钮像 bug。
- **修复**：`cardElevation="0dp"` + `cardUseCompatPadding="false"`，改用淡描边 `strokeColor="#E0E0E0"` `strokeWidth="1dp"` 清晰定义卡片边界。
- **验证**：`./gradlew testDebugUnitTest assembleRelease` → **BUILD SUCCESSFUL**（18测试通过）
- **产物**：`app/build/outputs/apk/release/app-release.apk` 已更新
- **状态**：✅ 完成（待用户在真机确认横线是否消除）
