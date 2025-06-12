import React, {useState} from 'react'
import {Alert, Button, Form, Modal} from 'react-bootstrap'
import api from '../../client/ApiClient'
import {useAuth} from '../../context/TokenContext'
import {useToast} from '../utils/useToast'

const RegisterModal = ({show, onHide, onSuccess}) => {
    const {login} = useAuth()
    const {addSuccess} = useToast()

    const [formData, setFormData] = useState({
        username: '', password: '', confirmPassword: ''
    })
    const [error, setError] = useState('')
    const [isLoading, setIsLoading] = useState(false)

    const handleChange = (e) => {
        const {name, value} = e.target
        setFormData(prev => ({...prev, [name]: value}))
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        setIsLoading(true)
        setError(null)

        if (formData.password === '') {
            setError('Введите пароль')
            setIsLoading(false)
            return
        }
        if (formData.password !== formData.confirmPassword) {
            setError('Пароли не совпадают')
            setIsLoading(false)
            return
        }

        api.register({username: formData.username, password: formData.password}).then((response => {
            login(response.token)
            addSuccess("Ваш аккаунт успешно создан")
            onHide()
        })).catch((err) => {
            setError(err.message)
        }).finally(() => {
            setIsLoading(false)
        })
    }

    return (<Modal show={show} onHide={onHide}>
        <Modal.Header closeButton>
            <Modal.Title>Регистрация</Modal.Title>
        </Modal.Header>

        <Form onSubmit={handleSubmit}>
            <Modal.Body>
                {error && <Alert variant="danger">{error}</Alert>}
                <Form.Group>
                    <Form.Label>Имя пользователя</Form.Label>
                    <Form.Control
                        type="text"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                    />
                </Form.Group>
                <Form.Group>
                    <Form.Label>Пароль</Form.Label>
                    <Form.Control
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                    />
                </Form.Group>
                <Form.Group>
                    <Form.Label>Повторите пароль</Form.Label>
                    <Form.Control
                        type="password"
                        name="confirmPassword"
                        value={formData.confirmPassword}
                        onChange={handleChange}
                    />
                </Form.Group>
            </Modal.Body>

            <Modal.Footer>
                <Button variant="outline-primary" onClick={onHide}>Назад</Button>
                <Button variant="primary" type="submit" disabled={isLoading}>
                    {isLoading ? 'Загрузка...' : 'Зарегистрироваться'}
                </Button>
            </Modal.Footer>
        </Form>
    </Modal>)
}

export default RegisterModal