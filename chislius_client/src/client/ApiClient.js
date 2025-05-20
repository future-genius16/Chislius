import axios from 'axios'

class ApiClient {
    test = true

    constructor(baseURL) {
        this.client = axios.create({
            baseURL, headers: {
                'Content-Type': 'application/json'
            }
        })
    }

    setAuthToken(token) {
        this.client.defaults.headers['x-user-token'] = token
    }

    clearAuthToken() {
        delete this.client.defaults.headers['x-user-token']
    }

    async login(request) {
        try {
            if (!this.test) {
                const response = await this.client.post('/users/login', request)
                return response.data
            } else {
                return {token: '12345678'}
            }
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async register(request) {
        try {
            if (!this.test) {
                const response = await this.client.post('/users/register', {
                    "username": "123",
                    "password": "123"
                })
                return response.data
            } else {
                return {token: '12345678'}
            }
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async logout() {
        try {
            if (!this.test) {
                await this.client.post('/api/users/logout')
            }
        } catch (error) {
            throw this.handleError(error)
        }
    }



    handleError(error) {
        console.log(error)
        if (error.response) {
            return new Error(error.response.data.detail || `Request failed with status ${error.response.status}`)
        } else if (error.request) {
            return new Error('No response received from server')
        } else {
            return new Error('Error setting up request')
        }
    }
}

const api = new ApiClient('http://127.0.0.1:8080/api')
export default api