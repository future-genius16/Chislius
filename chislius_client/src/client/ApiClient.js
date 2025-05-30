import axios from 'axios'

class ApiClient {
    test = false

    constructor(baseURL) {
        this.client = axios.create({
            baseURL, headers: {
                'Content-Type': 'application/json'
            }, timeout: 5000
        })
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

    async logout(token) {
        try {
            if (!this.test) {
                await this.client.post('/users/logout')
            }
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async update(token) {
        try {
            await this.client.get('/users/update', {headers: {'x-user-token': token}})
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async changeName(token, username) {
        try {
            console.log('1143234')
            await this.client.post('/users/username', {username: username}, {headers: {'x-user-token': token}})
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async createPrivateRoom(token, request) {
        try {
            const response = await this.client.post('/rooms', request, {headers: {'x-user-token': token}})
            return response.data
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async joinPrivateRoom(token, code) {
        try {
            await this.client.post('/rooms/join/' + code, null, {headers: {'x-user-token': token}})
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async joinPublicRoom(token) {
        try {
            await this.client.post('/rooms/join/public', null, {headers: {'x-user-token': token}})
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async leaveRoom(token) {
        try {
            await this.client.post('/rooms/leave', null, {headers: {'x-user-token': token}})
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async flipCard(token, id) {
        try {
            await this.client.post('/rooms/flip/' + id, null, {headers: {'x-user-token': token}})
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async skipMove(token) {
        try {
            await this.client.post('/rooms/skip', null, {headers: {'x-user-token': token}})
        } catch (error) {
            throw this.handleError(error)
        }
    }

    async submitMove(token) {
        try {
            await this.client.post('/rooms/submit', null, {headers: {'x-user-token': token}})
        } catch (error) {
            throw this.handleError(error)
        }
    }

    handleError(error) {
        console.log(error)
        if (error.code === 'ECONNABORTED') {
            console.log(error)
            return new Error('Превышено время ожидания ответа от сервера')
        }
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