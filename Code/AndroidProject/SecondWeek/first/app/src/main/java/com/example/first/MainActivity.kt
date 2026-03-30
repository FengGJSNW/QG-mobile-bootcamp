package com.example.first

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.RoundedCornerShape

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Screen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // 使用了 Material3 中仍处于实验阶段的 API（比如某些 Drawer/TopBar 组件）
@Composable
fun Screen() {

    val listState = rememberLazyListState()

    val rawPercent by remember {
        derivedStateOf { scrollToPercent(listState, rangePx = 300) }
    }


    val percent by animateFloatAsState(
        targetValue = rawPercent,
        animationSpec = tween(durationMillis = 180),
        label = "TopBarPercent"
    )

    val topBarColor = blendColor(
        start = Color.White,
        end = Color(0xFFE8DEF8),
        percent = percent
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                onClose = { scope.launch { drawerState.close() } }
            )
        }
    ) {

        Scaffold(
            topBar = {
                MyTopBar(
                    background = topBarColor,
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            },
            bottomBar = {
                MyBottomBar(percent = percent)
            }
        ) { innerPadding ->
            LazyColumn(
                state = listState,
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                // 10) 列表项：这里生成 10 张卡片
                items(20) { idx ->
                    InfoCard(
                        title = "Card #${idx + 1}",
                        subtitle = "subtitle",
                        leadingIcon = Icons.Filled.CheckCircle,
                        containerColor = Color(0xFFF4EFFA)
                    )
                }
            }
        }
    }
}

// 顶栏逻辑
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    background: Color = Color.White,
    onOpenDrawer: () -> Unit
) {
    Surface(
        color = background,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        TopAppBar(
            title = { Text("My Application") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            actions = {
                IconButton(onClick = { /* 先不做设置逻辑 */ }) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                }
                IconButton(onClick = onOpenDrawer) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "More")
                }
            }
        )
    }
}

// 颜色混合函数
fun blendColor(start: Color, end: Color, percent: Float): Color {
    val t = percent.coerceIn(0f, 1f) //转换并防止越界！！！
    return lerp(start, end, t)
}


//
fun scrollToPercent(state: LazyListState, rangePx: Int = 160): Float {
    // 只看第一项的滚动偏移；如果已经滚到第二项及以上，直接认为 100%
    val base = if (state.firstVisibleItemIndex > 3) rangePx else state.firstVisibleItemScrollOffset
    return (base.toFloat() / rangePx.toFloat()).coerceIn(0f, 1f)
}


// 底栏小图标
@Composable
fun CardIconButton(
    icon: ImageVector,
    contentDescription: String,
    containerColor: Color,
    iconTint: Color,
    onClick: () -> Unit
) {
    // 圆角矩形，可以设置圆角
    val myshape = RoundedCornerShape(28.dp)

    Surface(
        onClick = onClick,
        color = containerColor,
        shape = myshape,  // 3. 在这里应用新形状
        modifier = Modifier.size(56.dp) // 假设有个固定大小
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconTint
            )
        }
    }
}

// 底栏逻辑
@Composable
fun MyBottomBar(percent: Float) {
    // 背景色：白 -> 淡紫
    val barColor = blendColor(Color.White, Color(0xFFE8DEF8), 1.0f)

    // 图标卡片底：更淡一点（随 percent 增强）
    val chipColor = blendColor(Color(0xFFF3F3F3), Color(0xFFF4EFFA), 1.0f)

    // 图标颜色：深灰为主（你也可以让它随 percent 变化一点点）
    val iconTint = blendColor(Color(0xFF2B2B2B), Color(0xFF2B2B2B), percent)

    Surface(
        color = barColor,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CardIconButton(
                icon = Icons.Filled.Home,
                contentDescription = "Home",
                containerColor = chipColor,
                iconTint = iconTint,
                onClick = { }
            )
            CardIconButton(
                icon = Icons.Filled.Info,
                contentDescription = "About",
                containerColor = chipColor,
                iconTint = iconTint,
                onClick = { }
            )
            CardIconButton(
                icon = Icons.Filled.Settings,
                contentDescription = "Settings",
                containerColor = chipColor,
                iconTint = iconTint,
                onClick = { }
            )
        }
    }
}



// 卡片逻辑
@Composable
fun InfoCard(
    title: String,
    subtitle: String? = null,
    leadingIcon: ImageVector? = null,

    // ✅ 外观可选参数（有默认值）
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    shape: androidx.compose.ui.graphics.Shape = MaterialTheme.shapes.extraLarge,
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 0.dp),

    // ✅ 字体可选（不传就用默认 typography）
    titleStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.titleMedium,
    subtitleStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,

    // ✅ 左侧圆形图标底可选
    iconBackgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    iconTint: Color = MaterialTheme.colorScheme.onPrimaryContainer,

    // ✅ 背景图（不传则没有）
    backgroundPainter: Painter? = null,
    backgroundAlpha: Float = 0.18f,
    backgroundContentScale: ContentScale = ContentScale.Crop,

    // ✅ padding 可自定义
    innerPadding: PaddingValues = PaddingValues(18.dp),

    // ✅ 可插槽
    trailing: (@Composable () -> Unit)? = null,
    content: (@Composable () -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        elevation = elevation,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Box {
            // 背景图：只有传了 backgroundPainter 才会画
            if (backgroundPainter != null) {
                Image(
                    painter = backgroundPainter,
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = backgroundContentScale,
                    alpha = backgroundAlpha
                )
            }

            Row(
                modifier = Modifier.padding(innerPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    Surface(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape),
                        color = iconBackgroundColor
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = leadingIcon,
                                contentDescription = null,
                                tint = iconTint
                            )
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = title, style = titleStyle)

                    if (subtitle != null) {
                        Spacer(Modifier.height(2.dp))
                        Text(text = subtitle, style = subtitleStyle)
                    }

                    if (content != null) {
                        Spacer(Modifier.height(8.dp))
                        content()
                    }
                }

                if (trailing != null) {
                    Spacer(Modifier.width(12.dp))
                    trailing()
                }
            }
        }
    }
}



// 侧边栏逻辑
@Composable
fun AppDrawer(onClose: () -> Unit) {
    val config = LocalConfiguration.current
    val halfWidth = (config.screenWidthDp / 2).dp

    Surface(
        modifier = Modifier
            .width(halfWidth)
            .fillMaxHeight(),   // ✅ 关键：撑满屏幕高度
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            DrawerTopBar(onClose = onClose)

            // ✅ 关键：让列表占满剩余空间
            DrawerCardList(
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DrawerTopBar(onClose: () -> Unit) {
    Surface(
        color = Color(0xFFE8DEF8), // 你喜欢也可改成随滚动渐变
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Menu", style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = onClose) { Text("Close") }
        }
    }
}

@Composable
fun DrawerCardList(modifier: Modifier = Modifier) {
    val items = listOf("授权管理", "启动方式", "终端使用", "关于")

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { title ->
            InfoCard(
                title = title,
                subtitle = "点击进入（先不做事件）",
                leadingIcon = Icons.Filled.CheckCircle,
                containerColor = Color(0xFFF4EFFA)
            )
        }
    }
}