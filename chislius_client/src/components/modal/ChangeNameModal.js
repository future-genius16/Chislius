import React, {useState} from 'react'
import {Alert, Button, Form, Modal} from 'react-bootstrap'
import api from '../../client/ApiClient'
import {useAuth} from '../../context/TokenContext'
import {useToast} from '../utils/useToast'

const ChangeNameModal = ({show, onHide}) => {
    const {token} = useAuth()
    const {addSuccess} = useToast()

    const [formData, setFormData] = useState({username: ''})
    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState('')

    const handleChange = (e) => {
        const {name, value} = e.target
        setFormData(prev => ({...prev, [name]: value}))
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        setIsLoading(true)
        setError('')

        api.changeName(token, formData.username).then(() => {
            console.log("Hide")
            addSuccess("Имя пользователя успешно изменено")
            onHide()
        }).catch((err) => {
            console.log("Error")
            setError(err.message)
        }).finally(() => {
            setIsLoading(false)
        })
    }

    return (<Modal show={show} onHide={onHide}>
        <Modal.Header closeButton>
            <Modal.Title>Смена имени пользователя</Modal.Title>
        </Modal.Header>

        <Form onSubmit={handleSubmit}>
            <Modal.Body>
                {error && <Alert variant="danger">{error}</Alert>}
                <Form.Group className="mb-3">
                    <Form.Label>Новое имя пользователя</Form.Label>
                    <Form.Control
                        type="text"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                    />
                </Form.Group>
            </Modal.Body>

            <Modal.Footer>
                <Button variant="outline-primary" onClick={onHide} disabled={isLoading}>
                    Отмена
                </Button>
                <Button variant="primary" type="submit" disabled={isLoading}>
                    {isLoading ? 'Сохранение...' : 'Сохранить изменения'}
                </Button>
            </Modal.Footer>
        </Form>
    </Modal>)
}

export default ChangeNameModal