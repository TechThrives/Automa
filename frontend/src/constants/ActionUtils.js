const Triggers = [
    "TIME",
]

const Actions = [
    "SENDMAIL",
]

const isTrigger = (actionType) => Triggers.includes(actionType);
const isAction = (actionType) => Actions.includes(actionType);

export { isTrigger, isAction };