package services

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"time"
)

// The monolith has no houses or users, so all its sensors are connected
// to one default house on behalf of one default user
const (
	defaultHouseID          = "3f1c2a9e-7b4d-4c8a-9e21-5d6f7a8b9c01"
	defaultUserID           = "7c9e6679-7425-40de-944b-e07fc1f90ae7"
	temperatureSensorTypeID = "5c2b8e1a-3f4d-4b6c-9a7e-0d1f2e3a4b51"
)

// DevicesService registers monolith sensors in the devices microservice
type DevicesService struct {
	BaseURL    string
	HTTPClient *http.Client
}

// connectDeviceRequest is the body of POST /devices, see api/devices-openapi.yaml
type connectDeviceRequest struct {
	HouseID      string `json:"houseId"`
	TypeID       string `json:"typeId"`
	SerialNumber string `json:"serialNumber"`
}

// deviceResponse is the part of the devices microservice response the monolith needs
type deviceResponse struct {
	ID string `json:"id"`
}

// NewDevicesService creates a new devices service
func NewDevicesService(baseURL string) *DevicesService {
	return &DevicesService{
		BaseURL: baseURL,
		HTTPClient: &http.Client{
			Timeout: 10 * time.Second,
		},
	}
}

// RegisterSensor connects a temperature sensor as a device and returns the device ID.
// The sensor ID becomes the serial number of the device.
func (s *DevicesService) RegisterSensor(sensorID int) (string, error) {
	body, err := json.Marshal(connectDeviceRequest{
		HouseID:      defaultHouseID,
		TypeID:       temperatureSensorTypeID,
		SerialNumber: strconv.Itoa(sensorID),
	})
	if err != nil {
		return "", fmt.Errorf("error encoding device request: %w", err)
	}

	req, err := http.NewRequest(http.MethodPost, s.BaseURL+"/devices", bytes.NewReader(body))
	if err != nil {
		return "", fmt.Errorf("error creating device request: %w", err)
	}
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("X-User-Id", defaultUserID)

	resp, err := s.HTTPClient.Do(req)
	if err != nil {
		return "", fmt.Errorf("error registering device: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusCreated {
		return "", fmt.Errorf("unexpected status code: %d", resp.StatusCode)
	}

	var device deviceResponse
	if err := json.NewDecoder(resp.Body).Decode(&device); err != nil {
		return "", fmt.Errorf("error decoding device response: %w", err)
	}

	return device.ID, nil
}
