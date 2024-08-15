// import axios from 'axios'
// import { MessageBox, Message } from 'element-ui'
// import store from '@/store'
// import { getToken } from '@/utils/auth'

// // create an axios instance
// const service = axios.create({
//   baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
//   // withCredentials: true, // send cookies when cross-domain requests
//   timeout: 5000 // request timeout
// })

// // request interceptor
// service.interceptors.request.use(
//   config => {
//     // do something before request is sent

//     if (store.getters.token) {
//       // let each request carry token
//       // ['X-Token'] is a custom headers key
//       // please modify it according to the actual situation
//       config.headers['X-Token'] = getToken()
//     }
//     return config
//   },
//   error => {
//     // do something with request error
//     console.log(error) // for debug
//     return Promise.reject(error)
//   }
// )

// // response interceptor
// service.interceptors.response.use(
//   /**
//    * If you want to get http information such as headers or status
//    * Please return  response => response
//   */

//   /**
//    * Determine the request status by custom code
//    * Here is just an example
//    * You can also judge the status by HTTP Status Code
//    */
//   response => {
//     const res = response.data

//     // if the custom code is not 20000, it is judged as an error.
//     if (res.code !== 20000) {
//       Message({
//         message: res.message || 'Error',
//         type: 'error',
//         duration: 5 * 1000
//       })

//       // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
//       if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
//         // to re-login
//         MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
//           confirmButtonText: 'Re-Login',
//           cancelButtonText: 'Cancel',
//           type: 'warning'
//         }).then(() => {
//           store.dispatch('user/resetToken').then(() => {
//             location.reload()
//           })
//         })
//       }
//       return Promise.reject(new Error(res.message || 'Error'))
//     } else {
//       return res
//     }
//   },
//   error => {
//     console.log('err' + error) // for debug
//     Message({
//       message: error.message,
//       type: 'error',
//       duration: 5 * 1000
//     })
//     return Promise.reject(error)
//   }
// )

// export default service


import router from '@/router'
import axios from 'axios'
import { getToken, setToken, removeToken } from './auth'
import ElementUI from 'element-ui'

const service = axios.create({
  timeout: 10000 // request timeout
})

// axios拦截器, 默认增加请求头token
service.interceptors.request.use(
  config => {
    const token = getToken()
    if (token) {
      config.headers.Authorization = token
    }
    return config
  },
  err => {
    return Promise.reject(err)
  }
)

// 防止同时弹出多个错误提示
let hasErrorMessage = false
const showErrorMessage = (response) => {
  if (!hasErrorMessage && response.data.message && response.data.message !== '用户未登录，请先登录') {
    ElementUI.Message.error(response.data.message)
    hasErrorMessage = true
    setTimeout(() => {
      hasErrorMessage = false
    }, 1000)
  }
}
// 更新token
const updateToken = (response) => {
  const oldToken = getToken()
  const newToken = response.headers.authorization
  if (newToken && oldToken !== newToken) {
    setToken(newToken)
  }
  // 检查是否需要移除token
  if (response.headers['remove-token']) {
    removeToken()
  }
}
// axios 接口错误拦截
service.interceptors.response.use(
  function(res) {
    updateToken(res)
    if (res.data && res.data.status && (res.data.status < 200 || res.data.status >= 300)) {
      showErrorMessage(res)
      return Promise.reject(res)
    } else {
      return res
    }
  },
  function(error) {
    if (![500, 400, 404].includes(error.response.status)) {
      updateToken(error.response)
      showErrorMessage(error.response)
      // 401 Unauthorized
      if (error.response.status === 401) {
        // UserUtils.resetUserStore()
        setTimeout(() => {
          const path = router.app.$route.path
          if (path === '/login') {
            location.reload()
          } else {
            router.push({ path: '/login' })
          }
        }, 1000)
      }
    }
    return Promise.reject(error)
  }
)

export default service
