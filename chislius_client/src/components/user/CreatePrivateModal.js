import React, {useState} from 'react'
import {Alert, Button, Form, Modal} from 'react-bootstrap'
import api from '../../client/ApiClient'

const CreatePrivateModal = ({show, onHide}) => {
    const [formData, setFormData] = useState({capacity: ''})
    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState('')

    const handleChange = (e) => {
        const {name, value} = e.target
        setFormData(prev => ({...prev, [name]: value}))
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        setIsLoading(true)
        setError('')

        try {
            await api.createPrivateRoom(formData)
            onHide()
        } catch (err) {
            setError(err.message)
        } finally {
            setIsLoading(false)
        }
    }

    return (<Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>Присоединиться к приватной комнате</Modal.Title>
            </Modal.Header>

            <Form onSubmit={handleSubmit}>
                <Modal.Body>
                    {error && <Alert variant="danger">{error}</Alert>}
                    <Form.Group className="mb-3">
                        <Form.Label>Количество игроков</Form.Label>
                        <Form.Control
                            type="text"
                            name="capacity"
                            value={formData.capacity}
                            onChange={handleChange}
                        />
                    </Form.Group>
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={onHide} disabled={isLoading}>
                        Отмена
                    </Button>
                    <Button variant="primary" type="submit" disabled={isLoading}>
                        {isLoading ? 'Создание...' : 'Создать'}
                    </Button>
                </Modal.Footer>
            </Form>
        </Modal>)
}

export default CreatePrivateModal