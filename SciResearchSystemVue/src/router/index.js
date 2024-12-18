import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/register',
    component: () => import('@/views/login/register'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index'),
        meta: { title: '首页', icon: 'dashboard', affix: true }
      },
      {
        path: 'personInfo',
        name: 'personInfo',
        component: () => import('@/views/personInfo'),
        meta: { title: '个人信息' },
        hidden: true
      },
      {
        path: 'passwordChange',
        name: 'passwordChange',
        component: () => import('@/views/keyInfoChange'),
        meta: { title: '修改用户名和密码' },
        hidden: true
      }
    ]
  }

  // {
  //   path: '/sys',
  //   component: Layout,
  //   redirect: '/sys/user',
  //   name: 'sysManage',
  //   meta: { title: '系统管理', icon: 'sys' },
  //   children: [
  //     {
  //       path: 'user',
  //       name: 'user',
  //       component: () => import('@/views/sys/user'),
  //       meta: { title: '用户管理', icon: 'userManage' }
  //     },
  //     {
  //       path: 'tree',
  //       name: 'Tree',
  //       component: () => import('@/views/sys/role'),
  //       meta: { title: '角色管理', icon: 'roleManage' }
  //     }
  //   ]
  // },

  // {
  //   path: '/test',
  //   component: Layout,
  //   redirect: '/test/test1',
  //   name: 'test',
  //   meta: { title: '测试模块', icon: 'form' },
  //   children: [
  //     {
  //       path: 'test1',
  //       name: 'test1',
  //       component: () => import('@/views/test/test1'),
  //       meta: { title: '功能1', icon: 'form' }
  //     },
  //     {
  //       path: 'test2',
  //       name: 'test2',
  //       component: () => import('@/views/test/test2'),
  //       meta: { title: '功能2', icon: 'form' }
  //     },
  //     {
  //       path: 'test3',
  //       name: 'test3',
  //       component: () => import('@/views/test/test3'),
  //       meta: { title: '功能3', icon: 'form' }
  //     }
  //   ]
  // },

  // // 404 page must be placed at the end !!!
  // { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
