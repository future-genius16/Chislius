import React, {useState} from 'react'
import {Alert, Button, Form, Modal} from 'react-bootstrap'
import api from '../../client/ApiClient'
import {useAuth} from '../../context/TokenContext'

const LoginModal = ({show, onHide}) => {
    const {login} = useAuth()
    const [formData, setFormData] = useState({username: '', password: ''})
    const [error, setError] = useState('')
    const [isLoading, setIsLoading] = useState(false)

    const handleChange = (e) => {
        const {name, value} = e.target
        setFormData(prev => ({...prev, [name]: value}))
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        setIsLoading(true)
        setError(null)

        try {
            const response = await api.login(formData)
            login(response.token)
            onHide()
        } catch (err) {
            setError(err.message)
        } finally {
            setIsLoading(false)
        }
    }

    return (<Modal show={show} onHide={onHide}>
        <Modal.Header closeButton>
            <Modal.Title>Вход</Modal.Title>
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
            </Modal.Body>

            <Modal.Footer>
                <Button variant="outline-primary" onClick={onHide}>Назад</Button>
                <Button variant="primary" type="submit" disabled={isLoading}>
                    {isLoading ? 'Загрузка...' : 'Войти'}
                </Button>
            </Modal.Footer>
        </Form>
    </Modal>)
}

export default LoginModal