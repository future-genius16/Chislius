import axios from 'axios'

class ApiClient {
    test = false

    constructor(baseURL) {
        this.client = axios.create({
            baseURL, headers: {
                'Content-Type': 'application/json'
            }
        })
    }

    setAuthToken(token) {
        this.client.defaults.headers.common['x-user-token'] = token
    }

    clearAuthToken() {
        delete this.client.defaults.headers.common['x-user-token']
    }

    async register(request) {
        try {
            const response = await this.client.post('/users/register', request)
            return response.data
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async login(request) {
        try {
            const response = await this.client.post('/users/login', request)
            return response.data
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async logout() {
        try {
            if (!this.test) {
                await this.client.post('/users/logout')
            }
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async update() {
        try {
            const response = await this.client.get('/users/update')
            return response.data
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async changeName(username) {
        try {
            await this.client.post('/users/username', {username: username})
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async createPrivateRoom(request) {
        try {
            const response = await this.client.post('/rooms', request)
            return response.data
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async joinPrivateRoom(code) {
        try {
            await this.client.post('/rooms/join/' + code)
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async joinPublicRoom() {
        try {
            await this.client.post('/rooms/join/public')
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async leaveRoom() {
        try {
            await this.client.post('/rooms/leave')
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async flipCard(id) {
        try {
            await this.client.post('/rooms/flip/' + id)
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async skipMove() {
        try {
            await this.client.post('/rooms/skip')
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async submitMove() {
        try {
            await this.client.post('/rooms/submit')
        } catch (error) {
            throw this.handleError(error)
        }
    }


    handleError(error) {
        console.log(error)
        if (error.response) {
            return new Error(error.response.data.detail || `Запрос выполнен с ошибкой. Код ${error.response.status}`)
        } else if (error.request) {
            return new Error('Ответ от сервера не был получен')
        } else {
            return new Error('Запрос составлен некорректно')
        }
    }
}

const api = new ApiClient('http://127.0.0.1:8080/api')
export default api