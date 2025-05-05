import React, {useCallback, useEffect, useState} from 'react'
import Board from '../board/Board'
import Form from '../form/Form'
import './GameScreen.css'

function GameScreen({userId, roomId, data}) {
    const [canSubmit, setCanSubmit] = useState(false)
    const [cardsList, setCardsList] = useState([])

    useEffect(() => {
        setCardsList(data.cards)
    }, [cardsList, data.cards])

    const handleSubmit = () => {
    }

    const handleSkip = () => {
    }

    return (<div className='main'>
        <Form
            onSubmit={handleSubmit}
            canSubmit={canSubmit}
            onSkip={handleSkip}
        />
        <Board cards={cardsList}/>
    </div>)
}

export default GameScreen