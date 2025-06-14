import axios from "axios";
import log from "loglevel";

log.setLevel("info");

const clientName = 'client1';
const customUrl = `http://localhost:8080/api/v1/custom`;
const filteredUrl = `http://localhost:8080/api/v1/filtered`;
const maxPayments = 150;
let current = 0;

do {
    current++;
    log.info(`Processing call ${current}...`);

    const result = await axios({
        method: 'post',
        url: `${customUrl}/${current}`,
        data: "some content",
        headers: {
            'X-Api-Client': clientName
        }
    }).catch((error) => {
        log.info('Completed call with status:', error.response?.status, ' on instance ', error.response?.headers['instance-id']);
    });

    if (result) {
        log.info('Completed call with status:', result.status, ' on instance ', result.headers['instance-id']);
    }

} while (current < maxPayments);

log.info("Process completed!!!")