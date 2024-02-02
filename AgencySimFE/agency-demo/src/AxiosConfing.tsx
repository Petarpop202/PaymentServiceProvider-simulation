import axios from "axios";

export const configureAxiosRequestInterceptors = () => {
  axios.interceptors.request.use(
    (config) => {
      config.withCredentials = true;
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );
};
