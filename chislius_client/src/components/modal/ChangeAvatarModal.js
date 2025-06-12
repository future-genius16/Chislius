import React, {useState} from 'react'
import {Alert, Button, Form, Image, Modal, ToggleButton} from 'react-bootstrap'
import api from '../../client/ApiClient'
import {useAuth} from '../../context/TokenContext'
import {useToast} from '../utils/useToast'

const ChangeAvatarModal = ({show, onHide}) => {
    const {token} = useAuth()
    const {addSuccess} = useToast()

    const [formData, setFormData] = useState({avatar: ''})
    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState('')

    const handleChange = (e) => {
        const {name, value} = e.target
        console.log(value)
        setFormData(prev => ({...prev, [name]: value}))
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        setIsLoading(true)
        setError('')

        api.changeAvatar(token, formData.avatar).then(() => {
            console.log('Hide')
            addSuccess("Аватар пользователя успешно изменен")
            onHide()
        }).catch((err) => {
            console.log('Error')
            setError(err.message)
        }).finally(() => {
            setIsLoading(false)
        })
    }

    return (<Modal show={show} onHide={onHide}>
        <Modal.Header closeButton>
            <Modal.Title>Смена аватара пользователя</Modal.Title>
        </Modal.Header>

        <Form onSubmit={handleSubmit}>
            <Modal.Body>
                {error && <Alert variant="danger">{error}</Alert>}
                <Form.Group className="mb-3">
                    <Form.Label>Ваш выбор: {formData.avatar &&
                        <Image src={'/images/profiles/' + formData.avatar + '.png'} roundedCircle
                               width={32} height={32}/>}
                    </Form.Label>
                    <div className="avatar-grid" style={{
                        display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '15px', marginTop: '15px'
                    }}>
                        {[...Array(12).keys()].map((num) => (<div key={num} style={{position: 'relative'}}>
                            <ToggleButton
                                type="radio"
                                id={`avatar-${num}`}
                                name="avatar"
                                value={num}
                                checked={formData.avatar === num}
                                onChange={handleChange}
                                style={{
                                    position: 'absolute',
                                    opacity: 0,
                                    width: '100%',
                                    height: '100%',
                                    cursor: 'pointer',
                                    zIndex: 1
                                }}
                            />
                            <label htmlFor={`avatar-${num}`}>
                                <Image src={'/images/profiles/' + num + '.png'} roundedCircle width={100} height={100}/>
                            </label>
                        </div>))}
                    </div>
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

export default ChangeAvatarModal