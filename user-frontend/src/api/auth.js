import axios from 'axios';
import md5 from 'js-md5';

/**
 * 登录
 * @param {Object} data {userName, password}
 */
export function login(data) {
  const params = {
    userName: data.userName,
    password: md5(data.password), // 使用 md5 加密
    platform: 2
  };

  return axios.post('/api/auth/login', params)
    .then((response) => response.data)
    .catch((error) => {
      console.error('Login failed:', error);
      throw error;
    });
}


/**
 * 退出登录
 */
export function logout() {
  return axios.post('/api/auth/logout')
    .then((response) => response.data)
    .catch((error) => {
      console.error('Logout failed:', error);
      throw error;
    });
}

/**
 * 检查用户名是否存在
 * @param {string} userName
 */
export function checkUserName(userName) {
  const params = { userName };
  const url = '/api/auth/user-name/exists?userName=' + userName
  return axios.post(url)
    .then((response) => response.data)
    .catch((error) => {
      console.error('Check username failed:', error);
      throw error;
    });
  // return axios.post('/api/auth/user-name/exists', { params })
  //   .then((response) => response.data)
  //   .catch((error) => {
  //     console.error('Check username failed:', error);
  //     throw error;
  //   });
}
