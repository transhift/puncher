package client

import (
	"encoding/gob"
	"log"
	"net"
	"os"
	"github.com/transhift/puncher/common/protocol"
	"fmt"
)

var targets = map[string]*client

type client struct {
	net.Conn

	version string
	logger  *log.Logger
	enc     *gob.Encoder
	dec     *gob.Decoder
}

type target struct {
	client

	ready chan<- *client
}

func New(conn net.Conn) *client {
	return &client{
		Conn:   conn,
		logger: logger(conn),
		enc:    gob.NewEncoder(conn),
		dec:    gob.NewDecoder(conn),
	}
}

func logger(conn net.Conn) *log.Logger {
	const Flags = log.Ldate | log.Ltime | log.LUTC | log.Lshortfile
	prefix := conn.RemoteAddr().String()
	return log.New(os.Stdout, prefix, Flags)
}

func (c *client) Handle() error {
	defer c.Conn.Close()

	// Expect NodeType.
	var nodeType protocol.NodeType
	if err := c.dec.Decode(&nodeType); err != nil {
		return err
	}

	switch nodeType {
	case protocol.TargetNode:
		return c.HandleTarget()
	case protocol.SourceNode:
		return c.HandleSource()
	default:
		return fmt.Errorf("invalid NodeType 0x%x", nodeType)
	}
}
