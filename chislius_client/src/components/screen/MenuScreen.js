import {Button, Container} from 'react-bootstrap'
import api from '../../client/ApiClient'
import React, {useState} from 'react'
import JoinPrivateModal from '../modal/JoinPrivateModal'
import CreatePrivateModal from '../modal/CreatePrivateModal'
import {useAuth} from '../../context/TokenContext'
import MenuNavbar from '../navbar/MenuNavbar'
import {useToast} from '../utils/useToast'
import RulesModal from '../modal/RulesModal'

function MenuScreen({player}) {
    const {token} = useAuth()
    const {addError} = useToast()

    const [showJoinPrivate, setShowJoinPrivate] = useState(false)
    const [showCreatePrivate, setShowCreatePrivate] = useState(false)
    const [showRules, setShowRules] = useState(false)

    const onJoinPublicClick = () => {
        api.joinPublicRoom(token).catch((err) => {
            addError(err.message)
        })
    }

    return (<>
        <MenuNavbar player={player}/>
        <Container className={'mt-5'}>
            <h1><b>Главное меню</b></h1>
            <div className="d-grid gap-3 mt-4">
                <Button
                    size="lg"
                    className="mb-3 w-100" onClick={onJoinPublicClick}>Присоединиться к публичной комнате</Button>
                <Button
                    size="lg"
                    className="mb-3 w-100" onClick={() => setShowJoinPrivate(true)}>Присоединиться к приватной
                    комнате</Button>
                <Button
                    size="lg"
                    className="mb-3 w-100" onClick={() => setShowCreatePrivate(true)}>Создать приватную комнату</Button>
                <Button
                    size="lg"
                    variant={'outline-primary'}
                    className="mb-3 w-100" onClick={() => setShowRules(true)}>Правила игры</Button>
            </div>
        </Container>
        <JoinPrivateModal
            show={showJoinPrivate}
            onHide={() => setShowJoinPrivate(false)}
        />
        <CreatePrivateModal
            show={showCreatePrivate}
            onHide={() => setShowCreatePrivate(false)}
        />
        <RulesModal
            show={showRules}
            onHide={() => setShowRules(false)}
        />
    </>)
}

export default MenuScreen