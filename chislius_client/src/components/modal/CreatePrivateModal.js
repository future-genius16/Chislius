import React, {useState} from 'react'
import {Alert, Button, ButtonGroup, Form, Modal, ToggleButton} from 'react-bootstrap'
import api from '../../client/ApiClient'
import {useAuth} from '../../context/TokenContext'

const CreatePrivateModal = ({show, onHide}) => {
    const {token} = useAuth()

    const [capacityValue, setCapacityValue] = useState('2')
    const [modeValue, setModeValue] = useState('0')
    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState('')


    const handleSubmit = (e) => {
        e.preventDefault()
        setIsLoading(true)
        setError('')

        api.createPrivateRoom(token, {capacity: capacityValue, mode: modeValue}).then(() => {
            onHide()
        }).catch((err) => {
            setError(err.message)
        }).finally(() => {
            setIsLoading(false)
        })
    }

    const modes = [{name: 'Легкий', value: '0'}, {name: 'Средний', value: '1'}, {name: 'Сложный', value: '2'}]
    const capacities = ['2', '3', '4', '5', '6']

    return (<Modal show={show} onHide={onHide}>
        <Modal.Header closeButton>
            <Modal.Title>Присоединиться к приватной комнате</Modal.Title>
        </Modal.Header>

        <Form onSubmit={handleSubmit}>
            <Modal.Body>
                {error && <Alert variant="danger">{error}</Alert>}
                <Form.Group className="mb-3">
                    <Form.Label>Выберите количество игроков</Form.Label><br/>
                    <ButtonGroup>
                        {capacities.map((capacity, idx) => (<ToggleButton
                            key={'capacity' + idx}
                            id={`capacity-${idx}`}
                            type="radio"
                            variant={'outline-primary'}
                            value={capacity}
                            checked={capacityValue === capacity}
                            onChange={(e) => setCapacityValue(e.currentTarget.value)}
                        >
                            {capacity}
                        </ToggleButton>))}
                    </ButtonGroup>
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>Выберите сложность</Form.Label><br/>
                    <ButtonGroup>
                        {modes.map((mode, idx) => (<ToggleButton
                            key={'mode' + idx}
                            id={`mode-${idx}`}
                            type="radio"
                            variant={'outline-primary'}
                            value={mode.value}
                            checked={modeValue === mode.value}
                            onChange={(e) => setModeValue(e.currentTarget.value)}
                        >
                            {mode.name}
                        </ToggleButton>))}
                    </ButtonGroup>
                </Form.Group>
            </Modal.Body>

            <Modal.Footer>
                <Button variant="outline-primary" onClick={onHide} disabled={isLoading}>
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