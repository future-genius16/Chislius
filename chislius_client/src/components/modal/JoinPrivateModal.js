import React, {useState} from 'react'
import {Alert, Button, Form, Modal} from 'react-bootstrap'
import api from '../../client/ApiClient'
import {useAuth} from '../../context/TokenContext'

const JoinPrivateModal = ({show, onHide}) => {
    const {token} = useAuth()

    const [formData, setFormData] = useState({code: ''})
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

        if (formData.code === '') {
            setError('Введите код комнаты')
            setIsLoading(false)
            return
        }

        api.joinPrivateRoom(token, formData.code).then(() => {
            onHide()
        }).catch((err) => {
            setError(err.message)
        }).finally(() => {
            setIsLoading(false)
        })
    }

    return (<Modal show={show} onHide={onHide}>
        <Modal.Header closeButton>
            <Modal.Title>Присоединиться к приватной комнате</Modal.Title>
        </Modal.Header>

        <Form onSubmit={handleSubmit}>
            <Modal.Body>
                {error && <Alert variant="danger">{error}</Alert>}
                <Form.Group className="mb-3">
                    <Form.Label>Код комнаты</Form.Label>
                    <Form.Control
                        type="text"
                        name="code"
                        value={formData.code}
                        onChange={handleChange}
                    />
                </Form.Group>
            </Modal.Body>

            <Modal.Footer>
                <Button variant="outline-primary" onClick={onHide} disabled={isLoading}>
                    Отмена
                </Button>
                <Button variant="primary" type="submit" disabled={isLoading}>
                    {isLoading ? 'Загрузка...' : 'Присоединиться'}
                </Button>
            </Modal.Footer>
        </Form>
    </Modal>)
}

export default JoinPrivateModal